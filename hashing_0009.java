// Karthik Ganeshula - kg496
//NJIT ID - 31520009

import java.io.File;
import java.util.*;

public class hashing_0009 {
	static int[] T;
	static char[] A;
	static int N;
	static ArrayList<String> commands;
	static int Alen = 0;


	public static void main(String[] args) {

		if (args.length == 1) {
			readFile(args[0]);
			processCommands();
			isEmpty();
			isFull();
		} else {
			System.out.println("Invalid Arguments");
			return;
		}
	}

	static void createNewTable(int n) {

		N = n;
		T = new int[N];
		A = new char[15*N];
		Arrays.fill(T,-1);
	}

	static void readFile(String fileName) {

		commands = new ArrayList<String>();

		try {
			Scanner sc = new Scanner(new File(fileName));
			while (sc.hasNext()) {
				String line = sc.nextLine();
				commands.add(line);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}

	}

	static void printCommands() {
		for(String command: commands) {
			System.out.print(command);
			System.out.println();
		}
	}


	static void processCommands() {

		for(int i=0; i < commands.size(); i++) {

			String line = commands.get(i) + "";
			String[] command = line.split(" ");
			int first = Integer.parseInt(command[0]);
			int second;
			switch(first) {

			case 14:
				second = Integer.parseInt(command[1]);
				createNewTable(second);
				break;
			case 10:
				String ele = command[1];
				//System.out.println("I am here " +  command[1]);
				boolean doubleStatus = insert(ele);
				if(doubleStatus){
					return;
				}
				break;
			case 13:
				printHashTable();
				break;
			case 12:
				search(command[1]);
				break;
			case 11:
				delete(command[1]);
				break;
			default:
				break;
			}
		}
	}

	static void delete(String ele) {

        int pos = search(ele);
        if(pos == -1) {
        	System.out.println("Element Not Found");
        	return;
        }

        int hashIndex = T[pos];
        for(int i=hashIndex;i< (hashIndex + ele.length());i++) {
        	A[i] = '*';
        }
        T[pos] = -1;
        System.out.println( " "+ ele + " Deleted from Slot: "+ pos);
	}

	static boolean compare(String ele, int startIndex) {

		//System.out.println(ele+ " Comparing with  "+ A[startIndex]);
		int n = ele.length();
		int endPos = startIndex + n;
        if(Alen < endPos){
            return false;
        }
        int temp = startIndex;
        int count = ele.length();
        String tempS = "";
        while(A[temp] != '\\' && count > 0) {
        	tempS += A[temp];
        	temp += 1;
        	count -= 1;
        }
        //System.out.println("tempS: "+tempS);
        if(ele.equals(tempS) && A[endPos] == '\\') {
            return true;
        }
        return false;
	}


	static void isEmpty() {

		if(Alen==0) {
			System.out.println("Array is Empty");
		} else {
			System.out.println("Array is Not Empty");
		}

		boolean isFilled = false;
		for(int value: T) {
			if(value != -1){
				isFilled = true;
				System.out.println("Table is Not Empty");
				break;
			}
		}

		if(!isFilled) {
			System.out.println("Table is Empty");
		}

	}

	static void isFull() {

		if(Alen >= A.length){
			System.out.println("Array is Full");
		} else {
			System.out.println("Array is not full");
		}


		//Check Table
		boolean isEmpty = false;
		for(int value: T) {
			if(value == -1){
				isEmpty = true;
				System.out.println("Table is Not Full");
				break;
			}
		}

		if(!isEmpty) {
			System.out.println("Table is Full");
		}
	}

	static int search(String s) {

		boolean searchStatus = false;
        int searchPosition = -1;

        for(int v=0;v<T.length;v++) {
            if(T[v] == -1){
                continue;
            } else if(compare(s, T[v])) {

 				searchStatus = true;
                searchPosition = v;
                System.out.println(s + " Element Found at: "+ v);
                break;
            } else {
            	continue;
            }
        }
        if(!searchStatus) {
            System.out.println(s + " Not Found");
        }
        return searchPosition;
	}
	

	static void printHashTable() {

		System.out.println("A: ");
		System.out.println(A);
		System.out.println();
		System.out.println("T: ");
		int index = 0;
		for(int hash: T) {
			System.out.print(" "+index + ": " +hash);
			System.out.println();
			index += 1;
		}
	}

	static int hashing(String s, int i) {

		int value = 0;
        for (int x = 0; x < s.length(); x++) {
			value += s.charAt(x);
        }
        value -= 4;
        value += i * i;
        value %= N;
		//System.out.println("String" + s + " Value: " +value);
        return value;
	}

	static void doubleSize(int N) {

		printHashTable();
		System.out.println("------------------- "+ N  + " ----------------------------");
		Alen = 0;
		commands.set(0,"14 "+N);
		printCommands();
		createNewTable(N);
		processCommands();
	}


	static boolean insert(String element) {

		Boolean insertionStatus = false;

		for(int probingC=0;probingC<N;probingC++) {
			int pos = hashing(element, probingC);
			if(T[pos] == -1){
				insertionStatus = true;
				T[pos] = Alen;
				for (int x = 0; x < element.length(); x++) {
					A[Alen] = element.charAt(x);
					Alen += 1;
        		}
				A[Alen] = '\\';
				Alen += 1;
				break;
			}
		}

		if(!insertionStatus) {
			doubleSize(2 * N);
			return true;
		}
		return false;
	}
}