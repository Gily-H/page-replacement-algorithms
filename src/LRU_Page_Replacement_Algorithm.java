import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRU_Page_Replacement_Algorithm {

  private final boolean ACCESS_ORDER = true;

  public int lru_algorithm(String inputFileName, int frameCapacity) {
    // HashMap backed by a Linked list to preserve insertion order
    // optional parameter to determine Access Order: true = LRU --> MRU
    // constant time insertion, deletion, retrieval and search operations
    // keep the load factor higher than initial capacity to prevent dynamic growth
    // Key: page number, Value = page number
    Map<Integer, Integer> frameMap = new LinkedHashMap<>(frameCapacity, frameCapacity + 1,
        ACCESS_ORDER) {
      // after insertion, determine if we should remove the eldest entry based on removeEldestEntry implementation
      @Override
      protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > frameCapacity;
      }
    };
    String line;
    int pageNumber;
    int pageFaultCount = 0;
    System.out.println("LRU Policy Page Replacement Algorithm\n");

    // try-with-resources closes stream after completion
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
      while (true) {
        line = reader.readLine(); // read a line of input from file
        if (line == null) { // no input remaining
          break;
        }

        pageNumber = Integer.parseInt(line); // convert string input from file to int
        if (frameMap.containsKey(pageNumber)) { // page already exists inside memory frame map

          // accessing the page will bump the page to the end of the list as the MRU
          System.out.printf("Successful request of Page: %d\n", frameMap.get(pageNumber));

        } else { // page fault -- unable to find page in memory frame map
          System.out.printf("Page fault while attempting to access page number: %d\n", pageNumber);
          pageFaultCount++;

          // add page when there is a free frame
          // this insertion will also trigger removal of LRU based on overridden function removeEldestEntry
          frameMap.putIfAbsent(pageNumber, pageNumber);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.printf("Unable to locate file with name: %s\n", inputFileName);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return pageFaultCount;
  }
}

class LRU_TEST {

  public static void main(String[] args) {
    LRU_Page_Replacement_Algorithm test = new LRU_Page_Replacement_Algorithm();
    System.out.printf("\nLRU - Total page faults: %d\n",
        test.lru_algorithm(Paging_Constants.REFERENCE_STRING,
            Paging_Constants.FRAME_SET_CAPACITY));
  }
}