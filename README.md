# cccedictreader
Java library to read CC-CEDICT cedict_ts.u8

CC-CEDICT by MDBG, can be obtained at https://www.mdbg.net/chinese/dictionary?page=cedict

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
    CCCEDICTReader cr = new CCCEDICTReader(
      new FileInputStream("C:\\cedict_ts.u8"));
    
    //read entries to array
    int size = Integer.parseInt(cr.getHeaderMap().get("entries"));
    CCCEDICTEntry[] entries = new CCCEDICTEntry[size];
    int i = 0;
    while (true) {
      CCCEDICTEntry ce = cr.nextEntry();
      if (ce == null)
        break;
      entries[i++] = ce;
    }
    
    //close the reader
    cr.getReader().close();
    
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
