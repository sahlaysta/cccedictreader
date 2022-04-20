# cccedictreader
Java library to read and parse CC-CEDICT cedict_ts.u8

### Usage example
```java
import java.io.FileInputStream;
import java.io.IOException;

import sahlaysta.cccedict.CCCEDICTEntry;
import sahlaysta.cccedict.CCCEDICTReader;
import sahlaysta.cccedict.CCCEDICTReaderException;

public class Main {
  public static void main(String[] args)
      throws IOException, CCCEDICTReaderException {
    //create the reader
    CCCEDICTReader c = new CCCEDICTReader(
      new FileInputStream("C:\\cedict_ts.u8"));
    
    //read entries to array
    int size = Integer.parseInt(c.getHeaderMap().get("entries"));
    CCCEDICTEntry[] entries = new CCCEDICTEntry[size];
    int i = 0;
    while (true) {
      CCCEDICTEntry ce = c.nextEntry();
      if (ce == null)
        break;
      entries[i++] = ce;
    }
    
    //close the reader
    c.getReader().close();
    
    //random entry
    int index = 5368;
    CCCEDICTEntry ce = entries[index];
    System.out.println(ce.getTraditional());
    System.out.println(ce.getSimplified());
    System.out.println(ce.getPronunciation());
    System.out.println(ce.getDefinitions());
    
    //Console output:
    /*
     * 交還
     * 交还
     * jiao1 huan2
     * [to return sth, to hand back]
     */
  }
}

```