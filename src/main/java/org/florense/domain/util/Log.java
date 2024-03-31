package org.florense.domain.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    private Long id;
    private String type;
    private String className;
    private String params;
    private String errorMessage;
}
