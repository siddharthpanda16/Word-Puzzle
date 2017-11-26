import java.util.Map.Entry;

/*import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;*/


// QuadraticProbing Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// void makeEmpty( )      --> Remove all items


/**
 * Probing table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 */
public class MyHashTable<AnyType>
{
    /**
     * Construct the hash table.
     */
    public MyHashTable( )
    {
        this( DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size the approximate initial size.
     */
    public MyHashTable( int size )
    {
        allocateArray( size );
        doClear( );
    }

    /**
     * Insert word into the hash table. If the item is
     * already present, do nothing.
     * @param x the item to insert.
     */
    public boolean insert( AnyType x ,boolean isPrefix)
    {
            // Insert x as active
        int currentPos = findPos( x );
        if( isActive( currentPos ) || isActivePrefix(currentPos))
            return false;

        if( array[ currentPos ] == null )
            ++occupied;
        array[ currentPos ] = new HashEntry<>( x, true,isPrefix );
        theSize++;
        
            // Rehash; see Section 5.5
        if( occupied > array.length / 2 )
            rehash( );
        
        return true;
    }

    /**
     * Insert a prefix of a word into the hash table. If the item is
     * already present, do nothing.
     * @param x the item to insert.
     */
    public boolean insertPrefix( AnyType x )
    {
            // Insert x as active
        int currentPos = findPos( x );
        if( isActivePrefix( currentPos )  || isActive(currentPos))
            return false;

        if( array[ currentPos ] == null )
            ++occupied;
        array[ currentPos ] = new HashEntry<>( x, true,true );
        theSize++;
        
            // Rehash; see Section 5.5
        if( occupied > array.length / 2 )
            rehash( );
        
        return true;
    }
    /**
     * Expand the hash table.
     */
    private void rehash( )
    {
        HashEntry<AnyType> [ ] oldArray = array;

            // Create a new double-sized, empty table
        allocateArray( 2 * oldArray.length );
        occupied = 0;
        theSize = 0;

            // Copy table over
        for( HashEntry<AnyType> entry : oldArray )
            if( entry != null && entry.isActive )
                insert( entry.element,entry.isPrefix );
   }

    /**
     * Method that performs quadratic probing resolution.
     * @param x the item to search for.
     * @return the position where the search terminates.
     */
    private int findPos( AnyType x )
    {
        //int offset = 1;
        int currentPos = myhash( x );
        
        while( array[ currentPos ] != null &&
                !array[ currentPos ].element.equals( x ) )
        {
            //currentPos += offset;  // Compute ith probe
            //offset += 2;
        	currentPos++;
            if( currentPos >= array.length )
                currentPos -= array.length;
            
            lastIndexVal=lastIndexVal>currentPos?lastIndexVal:currentPos;
        }
        
        return currentPos;
    }

    /**
     * Remove from the hash table.
     * @param x the item to remove.
     * @return true if item removed
     */
    public boolean remove( AnyType x )
    {
        int currentPos = findPos( x );
        if( isActive( currentPos ) )
        {
            array[ currentPos ].isActive = false;
            theSize--;
            return true;
        }
        else
            return false;
    }
    
    /**
     * Get current size.
     * @return the size.
     */
    public int size( )
    {
        return theSize;
    }
    
    /**
     * Get length of internal table.
     * @return the size.
     */
    public int capacity( )
    {
        return array.length;
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public boolean contains( AnyType x )
    {
        int currentPos = findPos( x );
        return isActive( currentPos );
    }
    
    public boolean containsPrefix( AnyType x )
    {
        int currentPos = findPos( x );
        return isActivePrefix( currentPos );
    }
    
    /**
     * Return true if currentPos exists and is active.
     * @param currentPos the result of a call to findPos.
     * @return true if currentPos is active.
     */
    private boolean isActivePrefix( int currentPos )
    {
        return array[ currentPos ] != null && array[ currentPos ].isActive && array[ currentPos ].isPrefix;
    }

    /**
     * Return true if currentPos exists and is active.
     * @param currentPos the result of a call to findPos.
     * @return true if currentPos is active.
     */
    private boolean isActive( int currentPos )
    {
        return array[ currentPos ] != null && array[ currentPos ].isActive && !array[ currentPos ].isPrefix;
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty( )
    {
        doClear( );
    }

    private void doClear( )
    {
        occupied = 0;
        for( int i = 0; i < array.length; i++ )
            array[ i ] = null;
    }
    
    private int myhash( AnyType x )
    {
        int hashVal = x.hashCode( );

        hashVal %= array.length;
        if( hashVal < 0 )
            hashVal += array.length;

        return hashVal;
    }
    
    public static class HashEntry<AnyType>
    {
        public AnyType  element;   // the element
        public boolean isActive;  // false if marked deleted
        public boolean isPrefix;  // true if prefix

        public HashEntry( AnyType e, boolean p )
        {
            this( e, true, p );
        }              

        public HashEntry( AnyType e, boolean i, boolean p)
        {
            element  = e;
            isActive = i;
            isPrefix = p;
        }
    }

    private static final int DEFAULT_TABLE_SIZE = 101;

    public HashEntry<AnyType> [ ] array; // The array of elements
    private int occupied;                 // The number of occupied cells
    private int theSize;                  // Current size
    public int lastIndexVal;

    /**
     * Internal method to allocate array.
     * @param arraySize the size of the array.
     */
    private void allocateArray( int arraySize )
    {
        array = new HashEntry[ nextPrime( arraySize ) ];
    }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     */
    private static int nextPrime( int n )
    {
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime( int n )
    {
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }

}