package jooq.custom.generator;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;

public class JPrefixGeneratorStrategy extends DefaultGeneratorStrategy {
    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        // 일반 Table 은 모두 J 를 접두어로
        if(mode == Mode.DEFAULT){
            return "J" + super.getJavaClassName(definition, mode);
        }

        // 나머지는 그냥 그대로 생성
        return super.getJavaClassName(definition, mode);
    }
}
