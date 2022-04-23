import java.awt.print.Pageable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MRU_Page_Replacement_Algorithm {

  public int mru_algorithm(String filename, int frameCapacity) {
    // mruList will keep track of which pages were accessed and order the list from LRU -> MRU
    // Will have a Node pointing to the head of the list and a Node pointing to the tail of the list
    LinkedList pageList = new LinkedList();
    HashMap<Integer, Node> frameMap = new HashMap<>();
    String line;
    int pageNumber;
    int pageFaultCount = 0;

    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      while (true) {
        line = reader.readLine();
        if (line == null) { // no input remaining
          break;
        }

        pageNumber = Integer.parseInt(line); // convert string input from file to int
        if (frameMap.containsKey(pageNumber)) { // page already exists inside memory frame

          // access the page to update when it was last used
          Node mru = frameMap.get(pageNumber);
          pageList.updateMostRecentlyUsed(mru);
          System.out.printf("Page Number: %d\n", mru.getPageNumber());

        } else { // page fault -- unable to find page in memory frame
          System.out.printf("Page fault with page number: %d\n", pageNumber);
          pageFaultCount++;

          // add page when there is a free frame
          Node newPage = new Node(pageNumber);
          frameMap.putIfAbsent(pageNumber, newPage);

          // if no free frames, remove the MRU page in the list
          if (pageList.getListSize() >= frameCapacity) {
            Node mru = pageList.removeMru();
            // update frame map to remove entry for MRU node that was deleted
            frameMap.remove(mru.getPageKey());
          }

          pageList.add(newPage);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.printf("Unable to locate file with name: %s\n", filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return pageFaultCount;
  }
}

class Node {

  private int pageNumber;
  private int pageKey;
  Node next;
  Node previous;

  public Node(int pageNumber) {
    this.pageNumber = pageNumber;
    this.pageKey = pageNumber;
  }

  public int getPageNumber() {
    return this.pageNumber;
  }

  public int getPageKey() {
    return this.pageKey;
  }
}

class LinkedList {

  Node head;
  Node tail;
  int size;

  public LinkedList() {
    this.size = 0;
  }

  public void add(Node nodeToAdd) {
    if (this.size == 0) {
      this.head = nodeToAdd;
      this.tail = this.head;
    } else if (this.size > 0) {
      this.tail.next = nodeToAdd;
      nodeToAdd.previous = this.tail;
      this.tail = nodeToAdd;
    }

    this.size++;
  }

  // when a page node is accessed shift it to the tail as the MRU page
  private void shiftMru(Node mru) {
    this.tail.next = mru;
    mru.previous = this.tail;
    this.tail = mru;
  }

  // shift a mru node from its position in the list to the tail of the list
  public void updateMostRecentlyUsed(Node mru) {
    Node previousNode;
    Node nextNode;
    if (mru.previous != null && mru.next != null) {
      previousNode = mru.previous;
      nextNode = mru.next;

      previousNode.next = nextNode;
      nextNode.previous = previousNode;
      this.shiftMru(mru);
    }
  }

  public Node removeMru() {
    Node toBeRemoved = this.tail;
    if (this.tail.previous != null) { // tail represents the MRU page
      this.tail = toBeRemoved.previous;
      this.tail.next = null;
      this.size--;
    } else { // tail.previous is null if there is only one page or not pages in the list
      this.tail = null;
      this.head = null;
      this.size = 0;
    }

    return toBeRemoved;
  }

  public int getListSize() {
    return this.size;
  }
}


class MRU_TEST {

  public static void main(String[] args) {
    MRU_Page_Replacement_Algorithm test = new MRU_Page_Replacement_Algorithm();
    System.out.printf("MRU - Total page faults: %d\n",
        test.mru_algorithm(Paging_Constants.REFERENCE_STRING, Paging_Constants.FRAME_SET_CAPACITY));
  }
}
