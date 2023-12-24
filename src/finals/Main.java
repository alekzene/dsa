package finals;

import java.util.*;

public class Main {
    static Random rand = new Random();
    static List<int[]> generatedArraysList = new ArrayList<>();
    static int[] mergedArray;
    static int[] rangeList;

    public static void main ( String[] args ) {

        /* GENERATE A RANDOM NUMBER OF ARRAYS WITH RANDOM SIZES AND VALUES */
        int numOfArraysToGenerate = rand.nextInt( 2, 5 ); // Generates up to 5 random arrays
        int numOfArraysGenerated = 0;
        while ( numOfArraysGenerated < numOfArraysToGenerate ) {
            int randomSize = rand.nextInt( 1, 10 ); // Generates arrays with up to 10 elements
            int[] randomArray = new int[ randomSize ];
            addRandomValuesToArray( randomArray, randomSize );
            Arrays.sort( randomArray ); // Dual-Pivot Quick Sort
            generatedArraysList.add( randomArray );
            numOfArraysGenerated++;

            // TEST BLOCK
            System.out.println( "Array " + numOfArraysGenerated + " generated." );
            System.out.println( "Size: " + randomSize );
        }

        // TEST BLOCK
        System.out.println( "\nTotal Arrays Generated: " + numOfArraysGenerated );
        for ( int[] array : generatedArraysList ) {
            for ( int element : array ) {
                System.out.print( element + " ");
            }
            System.out.println( " " );
        }

        // TODO: FIND SERIES OF COMMON NUMBERS AMONG ARRAYS WITH THE SHORTEST RANGE
        rangeList = getShortestRange( generatedArraysList );

        System.out.print( "Range List: " );
        for ( int num : rangeList ) {
            System.out.print( num + " " );
        }
        System.out.println( "\n" );
//        rangeList = new int[] { -4, 0, 1, 9, 11 }; // NOTE: DUMMY LIST // FIXME: CHANGE TO ARRAYLIST  // TODO: DELETE LATER
//        Arrays.sort( rangeList );

        // CHECK IF NUMBERS IN RANGE LIST CONTAIN A PRIME NUMBER
        if ( hasPrimeNumber( rangeList ) == true ) {
            System.out.println( "\nList Has Prime" );

            // MERGE ALL RANDOMLY GENERATED ARRAYS
            mergedArray = mergeArrays( generatedArraysList );

            // TEST BLOCK
            System.out.print( "\nMerged Array: " );
            for ( int num :  mergedArray ) {
                System.out.print( num + " ");
            }
            System.out.println( "\n" );

            // FIND ALL POSSIBLE SUB-ARRAYS WHOSE VALUES EQUAL 0
            List<int[]> subArraysWithZeroSum = getSubArraysWithZeroSum( mergedArray );

            // TEST BLOCK
            if ( subArraysWithZeroSum.isEmpty() ) {
                System.out.println( "No Sub-Arrays With Zero Sum Found.");
            }
            else {
                System.out.println( "\nSub-Arrays With Zero Sum: " );
                for ( int[] subArray :  subArraysWithZeroSum ) {
                    for ( int number : subArray ) {
                        System.out.print( number + " ");
                    }
                    System.out.println( "\n" );
                }
                System.out.println( "\n" );
            }
        }
        else {
            System.out.println( "\nList Has No Prime (Note: 0, 1, and negative nos. do not count as prime)" );

            // FIND FIRST AND LAST OCCURRENCE OF NUMBERS IN THE RANGE LIST BASED ON RANDOMLY GENERATED ARRAYS
            int listCount = 0;
            for ( int[] array : generatedArraysList  ) {
                listCount++;
                System.out.println( "\nARRAY " + listCount );
                for ( int numInRangeList : rangeList ) {
                    /* Used Binary Search to find the element in the array as well as its first and last occurrences */
                    if ( Arrays.binarySearch( array, numInRangeList ) >= 0 ) {
                        int firstOccurrenceIndex = findFirstOccurrence( array, numInRangeList );
                        System.out.println( "! The first occurrence of " + numInRangeList + " is at index " + firstOccurrenceIndex + "." );

                        int lastOccurrenceIndex = findLastOccurrence( array, numInRangeList );
                        System.out.println( "! The last occurrence of " + numInRangeList + " is at index " + lastOccurrenceIndex + "." );

                    }
                    else {
                        System.out.println( "Number " + numInRangeList + " not found." );
                    }
                }
            }
        }
    }

    /* END OF MAIN METHOD */

    public static int[] getShortestRange( List<int[]> listOfArrays ) {
        int[] shortestRange = new int[ listOfArrays.size() ];

        int[] mergedArray = mergeArrays( listOfArrays );
        Arrays.sort( mergedArray );

        int minRange = Integer.MAX_VALUE;
        for ( int i = 0; i < mergedArray.length - listOfArrays.size() + 1; i++ ) {
            int currentRange = mergedArray[ i + listOfArrays.size() - 1 ] - mergedArray[ i ];
            if ( currentRange < minRange ) {
                minRange = currentRange;
                System.arraycopy( mergedArray, i, shortestRange, 0, listOfArrays.size() );
            }
        }

        return shortestRange;
    }

    /* FINDS FIRST OCCURRENCE OF ELEMENT IN ARRAY USING BINARY SEARCH */
    public static int findFirstOccurrence ( int[] array, int num ) {
        int lowerIndex = 0;
        int higherIndex = array.length - 1;
        int firstOccurrenceIndex = -1;

        while ( lowerIndex <= higherIndex ) {
            int middleIndex = lowerIndex + ( higherIndex - lowerIndex ) / 2;

            if ( array[ middleIndex ] == num ) {
                firstOccurrenceIndex = middleIndex;
                higherIndex = middleIndex - 1;
            } else if ( array[ middleIndex ] < num ) {
                lowerIndex = middleIndex + 1;
            } else {
                higherIndex =  middleIndex - 1;
            }
        }
        return firstOccurrenceIndex;
    }

    /* FINDS LAST OCCURRENCE OF ELEMENT IN ARRAY USING BINARY SEARCH */
    public static int findLastOccurrence ( int[] array, int num ) {
        int lowerIndex = 0;
        int higherIndex = array.length - 1;
        int lastOccurrenceIndex = -1;

        while ( lowerIndex <= higherIndex ) {
            int middleIndex = lowerIndex + ( higherIndex - lowerIndex ) / 2;

            if ( array[ middleIndex ] == num ) {
                lastOccurrenceIndex = middleIndex;

                lowerIndex = middleIndex + 1;
            } else if ( array[ middleIndex ] < num ) {
                lowerIndex = middleIndex + 1;
            } else {
                higherIndex = middleIndex - 1;
            }
        }
        return lastOccurrenceIndex;
    }

    /*  ADDS RANDOM VALUES TO ANY ARRAY OF ANY SIZE */
    public static void addRandomValuesToArray( int[] randomArray, int randomArraySize ) {
        for ( int i = 0; i < randomArraySize; i++ ) {
            int randomValueToAdd = rand.nextInt( -50, 50 ); // Generates positive and negative values
            randomArray[i] = randomValueToAdd;
        }
    }

    /* CHECKS IF LIST HAS A PRIME ELEMENT */
    public static boolean hasPrimeNumber( int[] list ) {
        for ( int element : list ) {
            if ( isPrime( element ) == true ) {
                return true;
            }
        }
        return false;
    }

    /* CHECKS IF ELEMENT IN THE LIST IS A PRIME NUMBER.
    * Prime Number: Positive number w/no other factors except itself and 1
    * Note: Zero (0) and one (1) are neither prime nor composite */
    public static boolean isPrime( int element ) {
        if ( element > 1 ) {
            /* Checks if element has other factors aside from 1 and itself */
            for ( int i = element - 1; i < element && i > 1; i-- ) {
                if ( element % i == 0 ) {
                    return false;
                }
            }
            System.out.println( "First Prime Element Detected: " + element );
            return true;
        }
        return false;
    }

    /* ADDS ALL VALUES IN RANDOMLY GENERATED ARRAYS TO ONE BIG ARRAY (SORTED) */
    public static int[] mergeArrays( List<int[]> listOfArrays ) {

        // Create merged array
        int mergedArraySize = 0;
        for ( int[] array : listOfArrays ) {
            mergedArraySize += array.length;
        }
        int[] mergedArray = new int[ mergedArraySize ];

        // Add every element in from the generated arrays to the merged array
        int index = 0;
        for ( int[] array : listOfArrays ) {
            for ( int number : array ) {
                mergedArray[ index++ ] = number;
            }
        }

        // Sort mergedArray using Dual-Pivot Quick Sort
        Arrays.sort( mergedArray );
        return mergedArray;
    }

        // TODO: UPDATE
    public static List<int[]> getSubArraysWithZeroSum( int[] array ) {
        List<int[]> subArrayWithZeroSumList = new ArrayList<>();

        // Iterate over all possible starting indices
        for ( int i = 0; i < array.length; i++ ) {
            // Iterate over all possible ending indices
            for ( int j = i + 1; j <= array.length; j++ ) {
                // Get the subarray from index i to j
                int[] subarray = Arrays.copyOfRange( array, i, j );

                // Check if the sum of the subarray is zero
                if ( sum ( subarray ) == 0 ) {
                    subArrayWithZeroSumList.add( subarray );
                }
            }
        }
        return subArrayWithZeroSumList;
    }

    private static int sum( int[] arr ) {
        int sum = 0;
        for ( int num : arr ) {
            sum += num;
        }
        return sum;
    }
}
