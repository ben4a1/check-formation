package by.paramonov.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Check {
    private String output;
    private List<String> orderList;
}
