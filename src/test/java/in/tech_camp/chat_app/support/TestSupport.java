package in.tech_camp.chat_app.support;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;

public class TestSupport {
  public static Map<String, String> violationMap(Set<ConstraintViolation<?>> violationSet) {
    return violationSet.stream()
                .collect(Collectors.toMap(
                  v -> v.getPropertyPath().toString(),
                  v -> v.getMessage()));
  }
}
