package com.jerry.savior_web.utils;

import com.alibaba.nacos.common.http.param.MediaType;
import com.jerry.savior_common.util.ObjectMapperHelper;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 22454
 */
public class JsonResponseWritingHelper {
    private final static ObjectMapperHelper OBJECT_MAPPER_HELPER =
            SpringContextHelper.getBean(ObjectMapperHelper.class);

    @SneakyThrows
    public static void writeJsonResponse(HttpServletResponse response, Object... args) {
        response.setContentType(MediaType.APPLICATION_JSON);
        PrintWriter writer = response.getWriter();
        for (Object arg : args) {
            writer.write(OBJECT_MAPPER_HELPER.toJson(arg));
        }
        writer.flush();
    }
}
