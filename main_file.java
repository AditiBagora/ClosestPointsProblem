package Assignment1;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
class Point 
{
   // This class is used to define a point and set it co-ordinates
    double x_cordinate;
    double y_cordinate;

    Point()
    {
        x_cordinate=0;
        y_cordinate=0;
    }

    Point(double x,double y)
    {
       x_cordinate=x;
       y_cordinate=y;
    }
}

class PointCollection
{   
    // This class is designed as a helper class
    public Point[] pointsXSorted;
    public Point[] pointsYSorted;
    double min_partition_distance;
    double dl,dr,dc;
    Point[] closest_point_Pair;
    int lower_Strip_Index,upper_Strip_Index;

    PointCollection()
    {
       min_partition_distance=Double.MAX_VALUE;
       dl=Double.MAX_VALUE;dr=Double.MAX_VALUE;dc=Double.MAX_VALUE;
       closest_point_Pair=new Point[2];
       lower_Strip_Index=0;upper_Strip_Index=0;
    }
    //This method initializes points and takes points from the user
    //and sorts the collection based on x co-ordinate in ascending order 
    public void InitializePoints()
    {   int size=0;
        Scanner scanner= new Scanner(System.in); 
        do{
           System.out.println("How many points are there on the 2D plane?");
           size= scanner.nextInt();
           if(size<=1)
             System.out.println("Please enter more than a point");
          }while(size<=1);
          
        pointsXSorted=new Point[size];
        pointsYSorted=new Point[size];
        for(int i=0;i<size;++i)
        {
           System.out.print("Enter the coordinates of Point "+(i+1)+"\nx1:");
           double x1=scanner.nextDouble();
           System.out.print("Enter the coordinates of Point "+(i+1)+"\ny1:");
           double y1=scanner.nextDouble();
           pointsXSorted[i]=new Point(x1,y1);
           pointsYSorted[i]=pointsXSorted[i];
        }
        scanner.close();
        if(size>2)
         Sort();
    }
    //Wrapper method that internally calls the merge sort algorithm to performing
    //sorting on x co-ordinate and y co-orinate
    private void Sort() 
    {
        //merge_sort takes the array to be sorted,lower index,upper index and axis(x:0 y:1 i.e.,
        //pass value of axis=0 to sort on x or 1 to sort on y) as an input and sorts the points
        //using divide and conquer approach in O(nlogn) time 
        MergeSort.merge_sort(pointsXSorted, 0, pointsXSorted.length-1,0);
        MergeSort.merge_sort(pointsYSorted, 0, pointsYSorted.length-1,1);
    }
    //This method uses Euclidian distance formula : distance=((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1))½  where p1 (x1,y1) p2(x2,y2) are two points
    //to calculate distance between 2 points in 2D and returns the distance
    public double CalculateEuclidianDistance(Point p1,Point p2)
    {
         double distance=0;
         distance=Math.sqrt(Math.pow(p2.x_cordinate-p1.x_cordinate,2)+Math.pow(p2.y_cordinate-p1.y_cordinate,2));
         return distance;
    }
    //This method creates a strip from the central line within delta distance in both the directions
    //and sorts the points in the strip based on y co-ordinate
    public Point[] CreateStripWithinDeltaFromCenter(Point[] points,double delta,Point mid_point)
    {     
          var center_x=mid_point.x_cordinate;
          ArrayList<Point> strip=new ArrayList<Point>();
          for(int i=0;i<points.length;i++)                                    //c1n
          { 
            if(Math.abs(points[i].x_cordinate-center_x)<=delta)
               strip.add(points[i]);
          }
          Point[] stripPoints= new Point[strip.size()];
          strip.toArray(stripPoints);
          return stripPoints;
    }
    //This method finds whether the difference in y is less than delta or not given p1,p2 and delta
    public boolean IsYDiffMoreThanDelta(Point p1,Point p2,double delta)
    {
        if(Math.abs(p1.y_cordinate-p2.y_cordinate)>delta)
          return true;
        else
          return false;
    }
    //This method tries to find whether the delta can be improved considering pointsXSorted in the strip
    public double TryImproveDeltaGivenStripPoints(Point[] points,double delta)
    {
        min_partition_distance=delta;
        for(int i=0;i<points.length;i++)        //For every Pi in the strip assume it n 
         for(int j=i+1;j<points.length;++j)                     //For every Pj in the strip assume it n
          {
              if(IsYDiffMoreThanDelta(points[i], points[j], delta))  //reduces number of points to be checked as if y difference
                break;                                               //is greater than delta the distance is also greater than delta

              double distance=CalculateEuclidianDistance(points[i], points[j]);
              if(min_partition_distance>distance)                                     //Check if delta is improved update the variables and indices
              {
                dc=min_partition_distance=distance;
                closest_point_Pair[0]=points[i];closest_point_Pair[1]=points[j];
              }
          }
        return dc;    
    }
   //FindClosestPoints is a recursive method that takes inputs points(already sorted by x cordinate and by y) 
    //and size of the y sorted array and then creates two partitions left_ySortedPoints and rightySorted points
    //using the midpoint of x sorted array and computes minimum distance in the two partitions left and right(dl,dr), 
    //compute delta=min(dl,dr), Create a strip within delta range from mid and try to miminize the distance delta 
    //and called it dc.So the minimum distance can either lie on left side or right side or it can be within the strip.
    //The method returns minimum amongst the before mentioned distances.
    public double FindClosestPairOfPoints(Point[] points,Point[] pointsYSorted,int size)
    {
      //for size less than or equal to 3 we can find the distance in constant time
      if(size<=3)                                                  
      {
           return FindClosestPair(points);                           //c1
      }
      
      int mid_index=(size)/2;                                       //c2
      
      Point[] left_ySortedPoints=new Point[mid_index];        //Store left partition of array i.e. low to mid
      Point[] right_ySortedPoints=new Point[size-mid_index];  //Store right partition of array i.e. mid+1 to upper
      int left_partition_index=0,right_partition_index=0;
      //This loop segregates according to mid point in the array sorted by x coordinate and divides the array sorted
      //in y in two partitions such that elements less than equal to x lies in left partition and the rest to right
      for(int i=0;i<size;i++)                                      //c3n              
      {  
         if(pointsYSorted[i].x_cordinate<points[mid_index].x_cordinate&&left_partition_index<mid_index)
           left_ySortedPoints[left_partition_index++]=pointsYSorted[i];
         else if(pointsYSorted[i].x_cordinate>points[mid_index].x_cordinate&&right_partition_index<size-mid_index)
           right_ySortedPoints[right_partition_index++]=pointsYSorted[i];
         else
         {
          if(pointsYSorted[i].y_cordinate<points[mid_index].y_cordinate&&left_partition_index<mid_index)
            left_ySortedPoints[left_partition_index++]=pointsYSorted[i]; 
          else
            if(right_partition_index<size-mid_index)
              right_ySortedPoints[right_partition_index++]=pointsYSorted[i];  
         }   
        
      }
      var dl=FindClosestPairOfPoints(Arrays.copyOfRange(points, 0, mid_index),left_ySortedPoints,left_ySortedPoints.length);           // T(n/2)
      var dr=FindClosestPairOfPoints(Arrays.copyOfRange(points, mid_index, size),right_ySortedPoints,right_ySortedPoints.length);         // T(n/2)
      
      var delta= Math.min(dl,dr);                                                                    // c4n
      
      var strip=CreateStripWithinDeltaFromCenter(pointsYSorted,delta,points[mid_index]);                               // c5n                                   
      var dc=TryImproveDeltaGivenStripPoints(strip,delta);                                  
      return Math.min(delta, dc);                       // T(n)=2T(n/2)+(c3+c4 +c5)n+c2+c1=2T(n/2) + O(n)=O(nlogn)
    }

    //Calculates minimum distance between points for size<=3
    private double FindClosestPair(Point[] points) 
    {
      for(int i=0;i<points.length;++i)
         for(int j=i+1;j<points.length;++j)
             { 
                 var distance= CalculateEuclidianDistance(points[i], points[j]);
                 if(distance<min_partition_distance)
                 {  
                   min_partition_distance=distance;
                   closest_point_Pair[0]=points[i];closest_point_Pair[1]=points[j];
                 }
             }
          return min_partition_distance;
    }
}

public class main_file
{
    public static void main(String[] args) 
    {
       PointCollection pointCollection= new PointCollection();
       pointCollection.InitializePoints();
       var minimumDistance=pointCollection.FindClosestPairOfPoints(pointCollection.pointsXSorted,
                              pointCollection.pointsYSorted,pointCollection.pointsXSorted.length);
       DecimalFormat f = new DecimalFormat("##.00");
       var closestPoints=pointCollection.closest_point_Pair;
       System.out.println("The closest pair of points are"+"("+closestPoints[0].x_cordinate+","+ 
       closestPoints[0].y_cordinate+") and ("+closestPoints[1].x_cordinate+","+ closestPoints[1].y_cordinate+")."+
       "The distance between them is "+ f.format(minimumDistance)  +" units");
    }
}