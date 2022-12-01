package Main.HTTPTaskServer;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(formatterWriter));
        } else jsonWriter.value("null");
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        if (!jsonReader.nextString().equals("null")) {
            return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
        } else {
            return null;
        }
    }
}