package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.converters;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListConverter {

    @TypeConverter
    public static String fromEventList(@NonNull List<String> list) {
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < list.size(); i++) {
            str.append(list.get(i));
            if(i < list.size() - 1) {
                str.append(",");
            }
        }

        return str.toString();
    }

    @TypeConverter
    public static List<String> toEventList(String str) {
        if(str == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {}.getType();
        String[] arr = str.split(",");
        return new ArrayList<>(Arrays.asList(arr));
    }
}
