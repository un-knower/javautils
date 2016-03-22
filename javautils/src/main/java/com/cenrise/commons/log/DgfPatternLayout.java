package com.cenrise.commons.log;

import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

public class DgfPatternLayout extends PatternLayout {

    public DgfPatternLayout() {
        this(DEFAULT_CONVERSION_PATTERN);
    }

    public DgfPatternLayout(String pattern) {
        super(pattern);
    }

    public PatternParser createPatternParser(String pattern) {
        PatternParser result;
        if (pattern == null)
            result = new DgfPatternParser(DEFAULT_CONVERSION_PATTERN);
        else
            result = new DgfPatternParser(pattern);

        return result;
    }

    static class DgfPatternParser extends PatternParser {

        static final char THROWS_CHAR = 'h';

        public DgfPatternParser(String pattern) {
            super(pattern);
        }

        public void finalizeConverter(char formatChar) {
            PatternConverter pc = null;
            switch (formatChar) {
            case THROWS_CHAR:
                pc = new DgfPatternConverter(formattingInfo);
                currentLiteral.setLength(0);
                addConverter(pc);
                break;
            default:
                super.finalizeConverter(formatChar);
            }
        }
    }

    static class DgfPatternConverter extends PatternConverter {

        DgfPatternConverter(FormattingInfo formattingInfo) {
            super(formattingInfo);
        }

        @Override
        protected String convert(LoggingEvent event) {
            StringBuffer buffer = new StringBuffer();
            String renderedMessage = event.getRenderedMessage();
            buffer.append(renderedMessage);

            String[] s = event.getThrowableStrRep();
            if (s != null) {
                int len = s.length;
                for (int i = 0; i < len; i++) {
                    buffer.append((s[i]));
                    buffer.append((Layout.LINE_SEP));
                }
            }
            return buffer.toString().replace("\"", "\"\"");// csv特殊字符的处理
        }

    }
}
