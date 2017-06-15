# TypedMap

[Maven usage]

	<project>
	  ...
	  <repositories>
	    <repository>
	      <id>qpg.jp</id>
	      <name>qpg.jp repository</name>
	      <url>http://qpg.jp/maven</url>
	    </repository>
	  </repositories>
	  <dependencies>
	    <dependency>
	      <groupId>jp.qpg</groupId>
	      <artifactId>TypedMap</artifactId>
	      <version>1.0.0</version>
	    </dependency>
	  </dependencies>
	</project>

[Example]

(Settings.java)

	import java.io.InputStreamReader;
	import java.io.Reader;
	import java.nio.charset.StandardCharsets;
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
	
	    Key<Integer> timeoutSeconds = map.key("timeoutSeconds", 30);
	
	    public static void main(String... __) {
	        System.out.println(Settings.title); /* 文字列表現でよければtoStringで取得可能 */
	        int timeout = Settings.timeoutSeconds.get();/* タイプセーフ */
	        System.out.println(timeout);
	    }
	}

(Run Settings)

	null
	30

(Prepare settings.properties)

	title = 型安全なプロパティ
	timeoutSeconds = 45

(Run Settings)

	型安全なプロパティ
	45
