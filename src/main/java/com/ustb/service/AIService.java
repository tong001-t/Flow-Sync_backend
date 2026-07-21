package com.ustb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * AI 服务 - 调用阿里云千问 DashScope API 进行任务拆解
 */
@Service
public class AIService {

    @Value("${dashscope.api-key}")
    private String apiKey;

    private static final String DASHSCOPE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = "你是一个项目管理助手。请根据用户提供的项目信息，将该项目的目标拆解为可执行的小任务。\n\n" +
            "要求：\n" +
            "1. 任务数量：3-7个\n" +
            "2. 每个任务包含：标题(title)、描述(description)、优先级(priority: 高/中/低)、建议完成天数(suggestedDays: 数字)、推荐负责人序号(assigneeId: 数字)\n" +
            "3. 输出一个summary字段概括整体方案\n" +
            "4. 严格输出JSON格式，不要markdown，不要任何额外文字\n" +
            "5. 每个任务都必须填写assigneeId，不能为空";

    private static final String SYSTEM_PROMPT_SUGGESTION = "你是一个直接的项目管理助手。请根据提供的任务信息给出具体的执行建议。\n\n" +
            "要求：\n" +
            "1. 执行的步骤建议\n" +
            "2. 执行顺序\n" +
            "3. 风险提示\n" +
            "4. 控制在300字以内\n" +
            "5. 严格输出JSON格式：{\"suggestion\": \"建议文本\"}，不要markdown，不要任何额外文字";

    public AIService() {
        this.restTemplate = new RestTemplate();
        // 设置 UTF-8 编码，解决千问 API 返回中文乱码问题
        this.restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 调用千问 API 生成任务拆解方案
     */
    public Map<String, Object> generateTaskPlan(String projectName, String goal, String description,
                                                 List<Map<String, Object>> members) {
        // 构建成员信息文本
        StringBuilder memberText = new StringBuilder();
        for (Map<String, Object> m : members) {
            memberText.append(m.get("id")).append(" - ")
                    .append(m.getOrDefault("realName", m.getOrDefault("username", "")))
                    .append("（").append(m.getOrDefault("role", "")).append("）\n");
        }

        String userPrompt = String.format("项目名称：%s\n任务目标：%s\n补充说明：%s\n可选成员（id - 姓名 - 角色）：\n%s\n\n请将该项目拆解为可执行的任务，返回JSON格式。",
                projectName, goal,
                (description == null || description.isEmpty()) ? "无" : description,
                memberText.toString());

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-plus");
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);
        messages.add(systemMsg);

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userPrompt);
        messages.add(userMsg);

        requestBody.put("messages", messages);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    DASHSCOPE_URL, HttpMethod.POST, entity, String.class);

            if (response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String content = root.path("choices").get(0).path("message").path("content").asText("");
                return parseAIResponse(content, members, goal);
            }
        } catch (Exception e) {
            System.err.println("千问 API 调用失败: " + e.getMessage());
        }

        return null;
    }

    /**
     * 调用千问 API 为单个任务生成执行建议
     * 文档 6.2：对单个任务给出执行步骤、顺序和风险提示
     */
    public String generateTaskSuggestion(String projectName, String taskTitle, String taskDescription) {
        String userPrompt = String.format("项目名称：%s\n任务标题：%s\n任务说明：%s\n\n请给出该任务的具体执行建议。",
                projectName, taskTitle,
                (taskDescription == null || taskDescription.isEmpty()) ? "无" : taskDescription);

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-plus");
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 500);

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT_SUGGESTION);
        messages.add(systemMsg);

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userPrompt);
        messages.add(userMsg);

        requestBody.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    DASHSCOPE_URL, HttpMethod.POST, entity, String.class);

            if (response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String content = root.path("choices").get(0).path("message").path("content").asText("");
                return parseSuggestionResponse(content);
            }
        } catch (Exception e) {
            System.err.println("千问 task-suggestion API 调用失败: " + e.getMessage());
        }

        return null;
    }

    /**
     * 解析 AI 返回的建议 JSON → 提取 suggestion 字段
     */
    private String parseSuggestionResponse(String content) {
        String jsonStr = content;

        // 尝试提取 markdown 代码块中的 JSON
        if (content.contains("```")) {
            int start = content.indexOf("```");
            int end = content.lastIndexOf("```");
            if (start != end) {
                start = content.indexOf("\n", start);
                if (start == -1) start = content.indexOf("```") + 3;
                jsonStr = content.substring(start + 1, end).trim();
                if (jsonStr.startsWith("json")) {
                    jsonStr = jsonStr.substring(4).trim();
                }
            }
        }

        try {
            JsonNode parsed = objectMapper.readTree(jsonStr);
            return parsed.path("suggestion").asText("");
        } catch (JsonProcessingException e) {
            // 如果 JSON 解析失败，直接把原始内容当作文本建议返回
            System.err.println("建议 JSON 解析失败，使用原始文本: " + e.getMessage());
            return content.length() > 300 ? content.substring(0, 300) : content;
        }
    }

    /**
     * 解析 AI 返回的 JSON
     */
    private Map<String, Object> parseAIResponse(String content, List<Map<String, Object>> members, String goal) {
        String jsonStr = content;

        // 尝试提取 markdown 代码块中的 JSON
        if (content.contains("```")) {
            int start = content.indexOf("```");
            int end = content.lastIndexOf("```");
            if (start != end) {
                start = content.indexOf("\n", start);
                if (start == -1) start = content.indexOf("```") + 3;
                jsonStr = content.substring(start + 1, end).trim();
                // 去掉可能的 "json" 标记
                if (jsonStr.startsWith("json")) {
                    jsonStr = jsonStr.substring(4).trim();
                }
            }
        }

        try {
            JsonNode parsed = objectMapper.readTree(jsonStr);

            List<Map<String, Object>> items = new ArrayList<>();
            JsonNode itemsNode = parsed.path("items");
            if (itemsNode.isArray()) {
                for (JsonNode item : itemsNode) {
                    Map<String, Object> task = new LinkedHashMap<>();
                    task.put("title", item.path("title").asText("未命名任务"));
                    task.put("description", item.path("description").asText(""));
                    String priority = item.path("priority").asText("中");
                    task.put("priority", Arrays.asList("高", "中", "低").contains(priority) ? priority : "中");
                    task.put("suggestedDays", item.path("suggestedDays").asInt(3));
                    // 验证 assigneeId
                    long assigneeId = item.path("assigneeId").asLong(0);
                    if (assigneeId == 0 && !members.isEmpty()) {
                        Object firstId = members.get(0).get("id");
                        assigneeId = firstId instanceof Number ? ((Number) firstId).longValue() : 1L;
                    }
                    task.put("assigneeId", assigneeId);
                    items.add(task);
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("summary", parsed.path("summary").asText(
                    "已为\"" + (goal != null ? goal : "项目") + "\"拆解出 " + items.size() + " 个任务"));
            result.put("items", items);
            return result;
        } catch (JsonProcessingException e) {
            System.err.println("AI JSON 解析失败: " + e.getMessage() + "\n原始内容: " + content);
            return null;
        }
    }

    /**
     * 生成兜底方案（当 AI 不可用时）
     */
    public Map<String, Object> buildFallbackPlan(String goal, List<Map<String, Object>> members) {
        List<Map<String, Object>> tasks = new ArrayList<>();

        String[][] taskDefs = {
                {"需求分析与确认", "明确目标边界、成功标准和关键需求", "高", "2"},
                {"方案设计与评审", "制定详细实施方案，分配资源和排期", "高", "3"},
                {"执行与实施", "按方案逐步推进各项工作", "中", "5"},
                {"质量检查与验收", "对成果进行检查和验收", "中", "2"},
                {"项目总结与复盘", "整理文档，总结经验教训", "低", "1"}
        };

        for (int i = 0; i < taskDefs.length; i++) {
            Map<String, Object> task = new LinkedHashMap<>();
            task.put("title", taskDefs[i][0]);
            task.put("description", taskDefs[i][1]);
            task.put("priority", taskDefs[i][2]);
            task.put("suggestedDays", Integer.parseInt(taskDefs[i][3]));
            // 轮流分配成员
            int memberIdx = i % members.size();
            task.put("assigneeId", members.get(memberIdx).get("id"));
            tasks.add(task);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summary", "针对\"" + goal + "\"已生成标准拆解方案（共 " + tasks.size() + " 个阶段）。建议根据实际需求调整顺序和内容。");
        result.put("items", tasks);
        return result;
    }
}
