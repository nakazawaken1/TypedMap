
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

import jp.qpg.TypedMap;
import jp.qpg.TypedMap.Key;

@SuppressWarnings("javadoc")
public interface Settings {
    TypedMap map = new TypedMap(() -> {
        String path = "settings.properties";
        Properties p = new Properties();
        try (Reader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(path), StandardCharsets.UTF_8)) {
            p.load(reader);
        } catch (Exception e) {
            Logger.getGlobal().warning("cannot load config file: " + path);
        }
        return p;
    });

    Key<String> title = map.key("title");

    Key<Integer> timeout = map.key("timeout", 30);

    Key<Locale> locale = map.key("locale", Locale.getDefault());

    Key<Charset> charset = map.key("charset", Charset.defaultCharset());

    Key<LocalDate> start = map.key("start", LocalDate.now());

    public static void main(String... __) {
        System.out.println(Settings.title); /* 文字列表現でよければtoStringで取得可能 */
        int timeout = Settings.timeout.get(); /* タイプセーフ */
        System.out.println(timeout);
        System.out.println();

        System.out.println("キー名 = " + Settings.title.name);
        System.out.println("値 = " + Settings.title);
        System.out.println("デフォルト値 = " + Settings.title.value);
        System.out.println("型 = " + Settings.title.type);
        System.out.println();
        System.out.println("キー名 = " + Settings.timeout.name);
        System.out.println("値 = " + Settings.timeout);
        System.out.println("デフォルト値 = " + Settings.timeout.value);
        System.out.println("型 = " + Settings.timeout.type);
        System.out.println();
        System.out.println("キー名 = " + Settings.locale.name);
        System.out.println("値 = " + Settings.locale);
        System.out.println("デフォルト値 = " + Settings.locale.value);
        System.out.println("型 = " + Settings.locale.type);
        System.out.println();
        System.out.println("キー名 = " + Settings.charset.name);
        System.out.println("値 = " + Settings.charset);
        System.out.println("デフォルト値 = " + Settings.charset.value);
        System.out.println("型 = " + Settings.charset.type);
        System.out.println();
        System.out.println("キー名 = " + Settings.start.name);
        System.out.println("値 = " + Settings.start);
        System.out.println("デフォルト値 = " + Settings.start.value);
        System.out.println("型 = " + Settings.start.type);
    }
}
