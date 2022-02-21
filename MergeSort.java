package Assignment1;
public class MergeSort
{
    /*T(n)=2T(n/2)+O(n)=O(nlogn) where n is size of array
    This is the main function that accepts the array and lower an upper index of the array.
    It finds the middle point to divide the array in each iteration using eq(1)*/
    public static void merge_sort(Point[] array,int lower_index,int upper_index,int axis)
    {   
        if(lower_index==upper_index)                        //Base case for 1 element
           return ;                                            
           
        int mid_index=(int)((lower_index+upper_index)/2);  
                         
        merge_sort(array, lower_index, mid_index,axis);        //T(n/2)
        merge_sort(array, mid_index+1, upper_index,axis);      //T(n/2)

        // Combine the arrays from low to mid,mid+1 to upper in ascending order for each division of input 
        combine(array,lower_index,mid_index,upper_index,axis); //O(n)
    }
    //sorts and combines the points in ascending order. Axis=0 combines based on x cordinate
    //Axis=1 sorts already sorted array of points by y coordinate.Thus if axis=0 it returns sorted points by x coordiate
    //and if axis=1 it returns an ordered pairs of (x,y).
    // This method takes T(n)=c1+c2+(C3+c4)*n/2+c5*n=O(n)
    private static void combine(Point[] array, int lower_index, int mid_index, int upper_index,int axis) 
    {
        Point[] left_partition=new Point[mid_index+1-lower_index]; //Store left partition of array i.e. low to mid  [c1]
        Point[] right_partition=new Point[upper_index-mid_index];  //Store right partition of array i.e. mid+1 to upper [c2]
 
        for(int i=lower_index,k=0;i<=mid_index;++i,++k)               // [c3*(n/2)]
             left_partition[k]=array[i];                             //Intializing left partition

        for(int i=mid_index+1,k=0;i<=upper_index;++i,++k)            // [c4*(n/2)] 
             right_partition[k]=array[i];                           //Intializing left partition

        int left_partition_index=0;int right_partition_index=0;     //Indices for left and right partition respectively
        for(int i=lower_index;i<=upper_index;++i)                   // [c5*n]
        {
            if(left_partition_index==left_partition.length && right_partition_index<right_partition.length)
            {
                array[i]=right_partition[right_partition_index];   //If the left partition is completely traversed  and
                ++right_partition_index;                           // right partition is left simply copy right partition 
            }
            else if(right_partition_index==right_partition.length && left_partition_index<left_partition.length)
            {
                array[i]=left_partition[left_partition_index];   //If the right partition is completely traversed  and 
                ++left_partition_index;                          //left partition is left simply copy right partition 
                
            }
            else
            {
                if(axis==0)
                {
                //Compare leftpartition elements with rightpartition element which ever is smaller store it in the array 
                 if(left_partition[left_partition_index].x_cordinate<=right_partition[right_partition_index].x_cordinate) 
                 {
                   array[i]=left_partition[left_partition_index];
                   ++left_partition_index;
                 }
                 else
                {
                  array[i]=right_partition[right_partition_index];
                  ++right_partition_index;
                }
               }
               else
               {
                    //Compare leftpartition elements with rightpartition element which ever is smaller store it in the array
                    if(left_partition[left_partition_index].y_cordinate<=right_partition[right_partition_index].y_cordinate) 
                    {
                     array[i]=left_partition[left_partition_index];
                     ++left_partition_index;
                    }
                    else
                    {
                     array[i]=right_partition[right_partition_index];
                     ++right_partition_index;
                    }
               }
            }
        }
       
    }
    public static void main(String[] args) 
    {
         Point[] array=new Point[3];
         array[0]= new Point(2,5);array[1]= new Point(3,3.5);array[2]= new Point(3,2);
         MergeSort.merge_sort(array, 0, array.length-1,0);
         MergeSort.merge_sort(array, 0, array.length-1,1);
         for(int i=0;i<array.length;++i)
          System.out.println("("+array[i].x_cordinate+","+array[i].y_cordinate+")");
    }
 }




 