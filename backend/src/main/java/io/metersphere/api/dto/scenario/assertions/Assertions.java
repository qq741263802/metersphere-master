package io.metersphere.api.dto.scenario.assertions;

import lombok.Data;

import java.util.List;

@Data
public class Assertions {
    private List<AssertionRegex> regex;
    private List<AssertionJsonPath> jsonPath;
    private AssertionDuration duration;
}
