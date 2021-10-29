package demo.annotationprocessordemo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.lib_annotation.Getter;

/**
 * @author guanliang
 * @since 2021/10/25
 */
@Getter
public class User {
    private String name = "segway";
    private int id = 5;
    private List<String> phoneNums = Arrays.asList("10010", "10086");
    private Map<String, String> map = new HashMap<>();
}
