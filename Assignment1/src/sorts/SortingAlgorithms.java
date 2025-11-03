package sorts;
import java.util.Random;


public class SortingAlgorithms 
{
	
	
	/*
	 * The idea of insertion sort is iterating through the array, and sorting the right element with into an already 
	 * sorted part of the array.
	 */
	
	public static void insertionSort(int[] arr) 
	{
		for(int i = 1; i < arr.length; i++) 
		{
			int key = arr[i];
			int j = i - 1;
			
			while(j >= 0 && arr[j] > key) 
			{
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = key;

		}
	}
	
	
	
	/*
	 * The idea of merge sort is dividing an array in to sub-arrays until size of one, and then calling a merge helper
	 * method that merges the left side of the array with the right side of the array.
	 * 
	 * The essence of this method is to break down the array into small arrays recursively until it reaches size of 1.
	 * After which, sorting 2 already sorted arrays becomes an easy task using the 2 pointer technique in merge()
	 * method.
	 */
	
	
	public static void mergeSort(int[] arr, int left, int right) 
	{
		if(left >= right) 
		{
			return;
		}
		
		int mid = (int) Math.floor((left + right)/2);
		mergeSort(arr, left, mid);
		
		mergeSort(arr, mid + 1, right);
		
		merge(arr, left, mid, right);
	}
	
	
	/*
	 * The merge method merges two arrays using the two pointer technique which compares values of the pointers, and
	 * takes the smallest value until both arrays are empty. Note that if a sub-array is already iterated through, then
	 * values of the other sorted sub-array would append in the combined array.
	 */
	
	private static void merge(int[] arr, int left, int mid, int right) 
	{
		int leftPointer = left;
	    int rightPointer = mid + 1;
	    int[] temp = new int[right - left + 1];
	    int i = 0;

	    while (leftPointer <= mid && rightPointer <= right) 
	    {
	        if (arr[leftPointer] <= arr[rightPointer]) 
	        {
	            temp[i++] = arr[leftPointer++];
	        } 
	        
	        else 
	        {
	            temp[i++] = arr[rightPointer++];
	        }
	    }

	    while (leftPointer <= mid) 
	    {
	        temp[i++] = arr[leftPointer++];
	    }

	    while (rightPointer <= right) 
	    {
	        temp[i++] = arr[rightPointer++];
	    }

	    for (int j = 0; j < temp.length; j++) 
	    {
	        arr[left + j] = temp[j];
	    }
		
	}
	
	/* Quicksort starts with a pivot element, and places in the right position with respect to the values of the rest
	 * of the elements. It does this by place all values greater than it on the right, and less then it on the left.
	 * 
	 * The Quicksort() method cuts the array by recursively calling partition the its sub left and right arrays.
	 * It does this until the size of the array is none.
	 * 
	 * The essence of this algorithm is that values smaller than a number already in the right number does not need to be
	 * redundantly compared with the values that are bigger than that number. Thus, dividing and conquering into 2
	 * sub-arrays that run the same task.
	*/
    public static void quickSort(int[] arr, int left, int right) 
    {
        if (left >= right) 
        {
            return;
        }

        int pivotIndex = partition(arr, left, right);
        quickSort(arr, left, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, right);
    }
    
    /*
     * The partition() method, places the pivot elements in the right position in the array by iterating through it and
     * swapping with values greater than it until all greater values are behind it. And then returns the position of the
     * pivot position for the Quicksort() method to partition it.
     */

    private static int partition(int[] arr, int left, int right) 
    {
        int pivotValue = arr[right];
        int i = left - 1;

        for (int j = left; j < right; j++) 
        {
            if (arr[j] <= pivotValue) 
            {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[right];
        arr[right] = temp;;
        return i + 1;
    }
    
    
    // ramdomArray creates a array of size "size" with a maximum called "bound"
    public static int[] randomArray(int size, int bound) 
    {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) 
        {
            arr[i] = rand.nextInt(bound);
        }
        return arr;
    }
    
    
    
    // creates a copy of the array
    public static int[] copy(int[] arr) 
    {
        int[] copy = new int[arr.length];
        for(int i = 0; i < arr.length; i++) 
        {
        	copy[i] = arr[i];
        }
        return copy;
    }
    

	public static void main(String[] args) 
	{
        // creating arrays
        int[] small = randomArray(10, 100);
        int[] large = randomArray(10000, 100000);

        // making the copies of the arrays
        int[] small1 = copy(small);
        int[] small2 = copy(small);
        int[] small3 = copy(small);

        int[] large1 = copy(large);
        int[] large2 = copy(large);
        int[] large3 = copy(large);

        long start, end;

        // calling sorting algorithms on the small arrays.
        start = System.nanoTime();
        quickSort(small1, 0, small1.length - 1);
        end = System.nanoTime();
        long smallQuick = end - start;

        start = System.nanoTime();
        mergeSort(small2, 0, small2.length - 1);
        end = System.nanoTime();
        long smallMerge = end - start;

        start = System.nanoTime();
        insertionSort(small3);
        end = System.nanoTime();
        long smallInsert = end - start;

        // calling sorting algorithms on the small arrays.
        start = System.nanoTime();
        quickSort(large1, 0, large1.length - 1);
        end = System.nanoTime();
        long largeQuick = end - start;

        start = System.nanoTime();
        mergeSort(large2, 0, large2.length - 1);
        end = System.nanoTime();
        long largeMerge = end - start;

        start = System.nanoTime();
        insertionSort(large3);
        end = System.nanoTime();
        long largeInsert = end - start;

        // Print results
        System.out.println("\n--- Sorting Benchmark Results ---");
        System.out.printf("Small (10 elements) QuickSort : %d ns%n", smallQuick);
        System.out.printf("Small (10 elements) MergeSort : %d ns%n", smallMerge);
        System.out.printf("Small (10 elements) InsertionSort: %d ns%n", smallInsert);

        System.out.println();

        System.out.printf("Large (10,000 elements) QuickSort : %d ns%n", largeQuick);
        System.out.printf("Large (10,000 elements) MergeSort : %d ns%n", largeMerge);
        System.out.printf("Large (10,000 elements) InsertionSort: %d ns%n", largeInsert);
	}

}
