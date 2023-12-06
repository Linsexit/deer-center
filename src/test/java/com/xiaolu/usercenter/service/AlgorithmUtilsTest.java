package com.xiaolu.usercenter.service;

import com.xiaolu.usercenter.utils.AlgorithmUtils;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/12/3 10:47
 * @Description 算法工具类测试
 */
@SpringBootTest
public class AlgorithmUtilsTest {

    @Test
    void test() {
        String str1 = "小鹿是狗";
        String str2 = "小鹿不是狗";
        String str3 = "小鹿是猫不是狗";
        String str4 = "小鹿是猫";
        // 最小编辑距离：字符串 1 通过最少多少次增删改字符的操作可以变成字符串 2
        int score1 = AlgorithmUtils.minDistance(str1, str2);
        int score2 = AlgorithmUtils.minDistance(str1, str3);
        int score3 = AlgorithmUtils.minDistance(str1, str4);
        // 1
        System.out.println(score1);
        // 3
        System.out.println(score2);
        // 1
        System.out.println(score3);
    }

    @Test
    void testCompareTags() {
        List<String> tagList1 = Arrays.asList("男","java","大一");
        List<String> tagList2 = Arrays.asList("男","python","大二");
        List<String> tagList3 = Arrays.asList("男","java","大一");
        List<String> tagList4 = Arrays.asList("猫娘","java","大一");

        List<Pair<List, Integer>> list = new ArrayList<>();

        // 最小编辑距离：字符串 1 通过最少多少次增删改字符的操作可以变成字符串 2
        int score1 = AlgorithmUtils.minDistance(tagList1, tagList2); // 2
        int score2 = AlgorithmUtils.minDistance(tagList1, tagList3); //         0
        int score3 = AlgorithmUtils.minDistance(tagList1, tagList4); //         1

        list.add(new Pair<>(tagList2, score1));
        list.add(new Pair<>(tagList3, score2));
        list.add(new Pair<>(tagList4, score3));

        List<Integer> top = list.stream()
                .sorted((a, b) -> a.getValue() - b.getValue())
                .map(Pair::getValue)
                .collect(Collectors.toList());

        // // 1
        // System.out.println(score1);
        // // 3
        // System.out.println(score2);
        // System.out.println(score3);
        top.forEach(System.out::println);
    }

}
