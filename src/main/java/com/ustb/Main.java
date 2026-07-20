package com.ustb;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.protocol.Protocol;

import java.util.Arrays;

public class Main {
    public static GenerationResult callWithMessage() throws ApiException, NoApiKeyException, InputRequiredException {
        // 以下为华北2（北京）地域的URL，各地域的URL不同。调用时请将{WorkspaceId}替换为真实的业务空间ID。
        Generation gen = new Generation(
                Protocol.HTTP.getValue(),
                "https://ws-nmak6dz396i2xaqw.cn-beijing.maas.aliyuncs.com/api/v1"
        );

        //配置用户提示词和系统提示词
        //系统提示词（系统级指令）：整个绘画期间的提示词
        //用户提示词（任务指令）：本次任务的提示词
        //定义系统提示词的（设定了AI的身份和通用规则）：项目的拆解一般由项目负责人负责完成
        String systemMessage="你是一个资深的项目专家，擅长将复杂项目拆解为可执行的任务单元。"+
                "你的工作原则："+
                "1.任务拆分遵循MECE原则（相互独立，完全穷尽"+
                "2.每个任务颗粒适中--既不能太粗（无法执行），也不能太细（过于繁琐）"+
                "3.任务描述需要具体、可操作，包含明确的动作和交付物"+
                "4.严格按照用户要求的格式进行输出，不添加任何多余的文字、解释、或markdown标记"+
                "5.不输出JSON以外的任何数据";
        //用户提示词：对人物的详细描述，可以在项目中允许用户通过项目输入框输入
        String userMessage="请将一下项目拆解为多个可执行任务，并以JSON数据输出。" +
                "##项目信息：" +
                "-项目名称名称：认知学习项目答辩" +
                "-可执行责任人：项目负责人、张三、李四" +
                "##拆解要求：" +
                "1.任务要求覆盖答辩的全流程（包含但不限于：答辩材料只能被、材料撰写、PPT制作、模拟演练、现场答辩）" +
                "2.每个任务必须包含以下字段，不可遗漏：" +
                "-projectName：任务描述（统一为\"认知学习项目答辩\")" +
                "-teakDesc：任务描述（需要包含\"做什么、交付什么、尽量简洁明了\"）" +
                "assignee：任务 责任人（从\"项目负责人、张三、李四\"中进行选择，合理分配" +
                "-status：状态（统一为\"未开始\"）" +
                "3.责任人分配原则：" +
                "- 合理分摊工作量，避免一个人承担过多工作" +
                "- 根据任务性质分配责任人（如：珠江我可以分配给擅长演讲的组员）" +
                "4.任务之间尽量独立，如果以来府岸西在teakDesc中进行说明" +
                "## 严格遵守数据的输出方式" +
                "仅输出一个JSON数据";
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(systemMessage)
                .build();

        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(userMessage)
                .build();

        //生成查询参数，包含：API-Key，模型，用户及系统提示词，返回数据的格式
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用阿里云百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                // 模型列表：https://help.aliyun.com/model-studio/getting-started/models
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }
    public static void main(String[] args) {
        try {
            GenerationResult result = callWithMessage();
            System.out.println(result.getOutput().getChoices().get(0).getMessage().getContent());
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.err.println("错误信息："+e.getMessage());
            System.out.println("请参考文档：https://help.aliyun.com/model-studio/developer-reference/error-code");
        }
        System.exit(0);
    }
}