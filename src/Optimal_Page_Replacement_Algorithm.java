import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Optimal_Page_Replacement_Algorithm {

  /**
   * @param inputFileName - name of file with page inputs
   * @param frameCapacity - available memory frame capacity
   * @return total number of page faults
   * @description method will implement the Optimal Page Replacement Algorithm
   * @function whenever there is a page fault, check to see which page in the memory frame will
   * appear last in the remaining page inputs
   */
  public int optimal_algorithm(String inputFileName, int frameCapacity) {
    System.out.print("Optimal Page Replacement Algorithm\n");
    int pageFaultCount = 0;
    String line;

    // create a Set to represent the memory frame
    Set<Integer> memoryFrame = new LinkedHashSet<>(frameCapacity);

    // read the page inputs from file and store inputs into an arraylist
    ArrayList<Integer> pageInputs = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
      while (true) {
        line = reader.readLine();
        if (line == null) {
          break;
        }

        pageInputs.add(Integer.valueOf(line));
      }
    } catch (FileNotFoundException e) {
      System.out.printf("Unable to locate file with name: %s\n", inputFileName);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // iterate over the page inputs
    for (int i = 0; i < pageInputs.size(); i++) {
      int currentPage = pageInputs.get(i);

      // if the page is already in the memory frame, log the page access = NO page fault
      if (memoryFrame.contains(currentPage)) {
        System.out.printf("Successfully accessed Page: %d\n", currentPage);

      } else { // page not found in the memory frame = YES page fault
        System.out.printf("\nPage Fault attempting to access Page: %d\n", currentPage);

        // if memory frame is full we need to open up an available frame
        if (memoryFrame.size() == frameCapacity) {
          // make a copy of the memory Frame and use it to track which page will appear last
          Set<Integer> copyOfMemoryFrame = new LinkedHashSet<>(memoryFrame);
          int pageToRemove = -1;

          // iterate over the remaining page inputs
          for (int j = i + 1; j < pageInputs.size(); j++) {
            int nextPage = pageInputs.get(j);

            // if we only have one page left, this will be the furthest page away in the list of inputs
            if (copyOfMemoryFrame.size() == 1) {
              // designate the remaining page to be the one removed from the memory frame
              pageToRemove = copyOfMemoryFrame.iterator().next();
              break;
            }

            // if we find a page that is in the memory frame, indicate that this will not be the furthest page
            if (copyOfMemoryFrame.contains(nextPage)) {
              // indicate by removing the page from the copy of the memory frame
              copyOfMemoryFrame.remove(nextPage);
            }
          }

          // if we have multiple pages that were not found in the remaining list of page inputs
          // just remove the one that came in last
          if (copyOfMemoryFrame.size() > 1) {
            // convert the copy of the memory frame into an array
            Integer[] arr = new Integer[copyOfMemoryFrame.size()];
            arr = copyOfMemoryFrame.toArray(arr);
            // indicate that we want to remove the last page found in the array
            pageToRemove = arr[arr.length - 1];
          }

          // remove the page that is the furthest away in the list of page inputs
          System.out.printf("Furthest Page to be removed from Memory Frame: %d\n", pageToRemove);
          memoryFrame.remove(pageToRemove);
        }

        // if empty frame available, add the page
        memoryFrame.add(currentPage);
        System.out.printf("Added Page %d to Memory Frame\n", currentPage);
        System.out.println("Memory Frame: " + memoryFrame.toString() + "\n");
        pageFaultCount++;
      }
    }

    return pageFaultCount;
  }
}

class OPTIMAL_TEST {

  public static void main(String[] args) {
    Optimal_Page_Replacement_Algorithm test = new Optimal_Page_Replacement_Algorithm();
    System.out.printf("\nOptimal - Total page faults: %d\n",
        test.optimal_algorithm(Paging_Constants.REFERENCE_STRING,
            Paging_Constants.FRAME_SET_CAPACITY));
  }
}