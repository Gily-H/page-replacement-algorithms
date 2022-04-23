import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class FIFO_Page_Replacement_Algorithm {

  public int fifo_algorithm(String inputFileName, int frameCapacity) {
    // Hashset backed by a linked list to preserve the insertion order
    // constant time contains, insertion, removal, size operations
    // keep the load factor higher than initial capacity so that the LinkedSet doesn't dynamically grow
    Set<Integer> frameSet = new LinkedHashSet<>(frameCapacity, frameCapacity + 1);
    String line;
    int pageNumber;
    int pageFaultCount = 0;
    System.out.println("FIFO Policy Page Replacement Algorithm\n");

    // try-with-resources closes stream after completion
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
      while (true) {
        line = reader.readLine();
        if (line == null) { // no input remaining
          break;
        }

        pageNumber = Integer.parseInt(line); // convert string input from file to int
        if (frameSet.contains(pageNumber)) { // page already exists inside memory frame set

          // log existing page in frame set
          System.out.printf("Successful request of Page: %d\n", pageNumber);

        } else { // page fault -- unable to find page in memory frame set
          System.out.printf("Page fault while attempting to access page number: %d\n", pageNumber);
          pageFaultCount++;

          // FIFO policy - if no free frame, kick the earliest entered page
          if (frameSet.size() == frameCapacity) {
            // remove the first element of the frame set
            frameSet.remove(frameSet.iterator().next());
          }

          frameSet.add(pageNumber); // add page when there is a free frame
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

class FIFO_TEST {
  public static void main(String[] args) {
    FIFO_Page_Replacement_Algorithm test = new FIFO_Page_Replacement_Algorithm();
    System.out.printf("\nFIFO - Total page faults: %d\n",
        test.fifo_algorithm(Paging_Constants.REFERENCE_STRING,
            Paging_Constants.FRAME_SET_CAPACITY));
  }
}
