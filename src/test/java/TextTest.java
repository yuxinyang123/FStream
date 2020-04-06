import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TextTest
 *
 * @author yuxinyang
 * @date 2020/4/7 1:15
 */
public class TextTest {

    @Test
    void wordCount() throws IOException {
        Map<String, Integer> resultMap =
                Files.lines(Paths.get("src/test/resources/Jane Eyre.txt"), Charset.forName("GBK"))
                        .flatMap(line -> Stream.of(line.split(" |\\p{P}|　　")))
                        .collect(Collectors.toMap(k -> k, v -> 1, (o, n) -> o + 1));

        Map<String, Integer> sortedResultMap = resultMap.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (o, n) -> (n), LinkedHashMap::new));
        sortedResultMap.forEach((k, v) -> System.out.println(String.format("(%s,%s)", k, v)));
    }
}
