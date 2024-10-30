package com.hit.service.impl;

import com.hit.pojo.Result;
import com.hit.service.SensitiveService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensitiveServiceImpl implements SensitiveService {
    @Override
    public Result add(List<String> sensitiveWords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\Code\\Python\\code\\checkSensitive\\data\\dict.txt", true))) {
            for (String word : sensitiveWords) {
                writer.write(word);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("写入文件失败");
        }
        return Result.success();
    }

    @Override
    public Result sub(int length) {
        File inputFile = new File("D:\\Code\\Python\\code\\checkSensitive\\data\\dict.txt");
        File tempFile = new File("D:\\Code\\Python\\code\\checkSensitive\\data\\tempDict.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)); BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            List<String> allLines = reader.lines().collect(Collectors.toList());
            int totalLines = allLines.size();
            int linesToKeep = totalLines - length;
            if (linesToKeep < 0) {
                return Result.error("删除的行数超过文件总行数");
            }
            for (int i = 0; i < linesToKeep; i++) {
                writer.write(allLines.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("删除文件内容失败");
        } // 替换原文件
        if (!inputFile.delete()) {
            return Result.error("无法删除原文件");
        }
        if (!tempFile.renameTo(inputFile)) {
            return Result.error("无法重命名临时文件");
        }
        return Result.success();
    }
}
