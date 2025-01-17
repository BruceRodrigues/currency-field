/*
 * Copyright 2015 okd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tm.kod.widgets.demo;

import com.vaadin.data.util.converter.Converter;
import java.util.Locale;

/**
 *
 * @author Kerim O.D.
 */
public class NumberFieldConverter implements Converter<String, Integer>{

    public NumberFieldConverter() {
        // default constructor
    }    
    
    @Override
    public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws ConversionException {
        try {
            return Integer.getInteger(value);
        }catch(IllegalArgumentException ex){
            throw new ConversionException(ex);
        }
    }

    @Override
    public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if(value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

}
