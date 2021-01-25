package rtproject;
import java.io.File; 
import java.io.IOException; 
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;


public class RTProject implements Runnable {
    String switchh;
    BufferedImage ImgI;
    BufferedImage ImgO;
    int startWidth;
    int startHeight;
    int bolme;
 
    public RTProject(BufferedImage imgI, int StartWidth, int StartHeight, String Switchh, int Bolme) {
        this.switchh = Switchh;
        this.ImgI = imgI;
        this.startWidth = StartWidth;
        this.startHeight = StartHeight;
        this.bolme = Bolme;
    }
    public RTProject(BufferedImage imgI, BufferedImage imgO, int StartWidth, int StartHeight, String Switchh, int Bolme) {
        this.switchh = Switchh;
        this.ImgI = imgI;
        this.ImgO = imgO;
        this.startWidth = StartWidth;
        this.startHeight = StartHeight;
        this.bolme = Bolme;
    }
    public void run(){
        if (switchh == "gray"){
            grayfonk(ImgI, startWidth, startHeight, bolme);
        }
        else if(switchh == "median"){
            //applying the filter 5 times for a more noticable result
            for(int i=0; i<5; i++){
            medianfonk(ImgI, startWidth, startHeight, bolme);
            }
        }
        else if(switchh == "brightness"){
            brightnessfonk(ImgI, startWidth, startHeight, bolme);
        }
        else if(switchh == "blur"){
            blurfonk(ImgI, startWidth, startHeight, bolme);
        }
        else if(switchh == "sepia"){
            sepiafonk(ImgI, startWidth, startHeight, bolme);
        }
        else if(switchh == "invert"){
            invertfonk(ImgI, startWidth, startHeight, bolme);
        }
        else if(switchh == "reverse"){
            reversefonk(ImgI,ImgO, startWidth, startHeight, bolme);
        }
    }
    

    public static synchronized void  reversefonk(BufferedImage img,BufferedImage imgO, int StartWidth, int StartHeight, int bolme) {
        int width = img.getWidth()/bolme; 
        int height = img.getHeight()/bolme; 

        for (int y = StartHeight; y < height+StartHeight; y++) 
        { 
            for (int x = StartWidth; x < width+StartWidth; x++) 
            { 
                // Here (x,y)denotes the coordinate of image  
                // for modifying the pixel value. 
                int p = img.getRGB(x,y); 
                int a = (p>>24)&0xff; 
                int r = (p>>16)&0xff; 
                int g = (p>>8)&0xff; 
                int b = p&0xff; 
  
                int avg = (r+g+b)/3; 
                p = (p & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
                
                imgO.setRGB(y, x, p);
            }
        }
    }
    
    public static synchronized void  grayfonk(BufferedImage img, int StartWidth, int StartHeight, int bolme) {
        int width = img.getWidth()/bolme; 
        int height = img.getHeight()/bolme; 
     
        for (int y = StartHeight; y < height+StartHeight; y++) 
        { 
            for (int x = StartWidth; x < width+StartWidth; x++) 
            { 
                // Here (x,y)denotes the coordinate of image  
                // for modifying the pixel value. 
                int p = img.getRGB(x,y); 
                int a = (p>>24)&0xff; 
                int r = (p>>16)&0xff; 
                int g = (p>>8)&0xff; 
                int b = p&0xff; 
  
                // calculate average 
                int avg = (r+g+b)/3; 
  
                // replace RGB value with avg 
                p = (a<<24) | (avg<<16) | (avg<<8) | avg; 
  
                img.setRGB(x, y, p);
            }
        }
    }
    
    
    public static synchronized void medianfonk(BufferedImage img, int StartWidth, int StartHeight, int bolme) {
        int width = img.getWidth()/bolme; 
        int height = img.getHeight()/bolme; 
        int[] p = new int[9];
        int r=0;
        int g=0;
        int b=0;
        for(int i = StartWidth+1; i < width+StartWidth-1; i++)
            for(int j = StartHeight+1; j < height+StartHeight-1; j++)
            {
               p[0] = img.getRGB(i-1,j-1);
               p[1] = img.getRGB(i,j-1);
               p[2] = img.getRGB(i+1,j-1);
               p[3] = img.getRGB(i-1,j);
               p[4] = img.getRGB(i,j);
               p[5] = img.getRGB(i+1,j);
               p[6] = img.getRGB(i-1,j+1);
               p[7] = img.getRGB(i,j+1);
               p[8] = img.getRGB(i+1,j+1);
                                    
               for(int x=0; x<9; x++){
                    int a = (p[x]>>24)&0xff; 
                    
                   // Get the individual colors
                    r = (p[x] >> 16) & 0xff;
                    g = (p[x] >> 8) & 0xff;
                    b = p[x]&0xff;
                   // Check the boundaries
                   r = Math.min(Math.max(0, r), 255);
                   g = Math.min(Math.max(0, g), 255);
                   b = Math.min(Math.max(0, b), 255);
                   // Return the result
                p[x] = (a<<24) | (r<<16) | (g<<8) | b;
                }
                Arrays.sort(p);
                img.setRGB(i,j, p[4]); 
            
        }
        //  Calling a function to apply the filter vertically and horizontally
        //  on the surrounding parts of every square.
              medianfonkKenar(img,StartWidth, StartHeight, bolme);
       }

        public static synchronized void medianfonkKenar(BufferedImage img, int StartWidth, int StartHeight,int bolme) {
        int width = img.getWidth()/bolme; 
        int height = img.getHeight()/bolme;
        int r=0;
        int g=0;
        int b=0;
        int[] p = new int[9];
        
    //  function to go over the parts vertically
    try{
        for(int i = StartWidth+width-10; i < width+StartWidth+10; i++)
            for(int j = 1; j < img.getHeight()-1; j++)
            {

               p[0] = img.getRGB(i-1,j-1);
               p[1] = img.getRGB(i,j-1);
               p[2] = img.getRGB(i+1,j-1);
               p[3] = img.getRGB(i-1,j);
               p[4] = img.getRGB(i,j);
               p[5] = img.getRGB(i+1,j);
               p[6] = img.getRGB(i-1,j+1);
               p[7] = img.getRGB(i,j+1);
               p[8] = img.getRGB(i+1,j+1);
             
               
               for(int k=0; k<9; k++){
                    int a = (p[k]>>24)&0xff; 
                   // Get the individual colors
                    r = (p[k] >> 16) & 0xff;
                    g = (p[k] >> 8) & 0xff;
                    b = p[k]&0xff;
                   // Check the boundaries
                   r = Math.min(Math.max(0, r), 255);
                   g = Math.min(Math.max(0, g), 255);
                   b = Math.min(Math.max(0, b), 255);
                    // Return the result
                   p[k] = (a<<24) | (r<<16) | (g<<8) | b;
                    }
                Arrays.sort(p);
                img.setRGB(i,j, p[4]); 
                }
    
    
        //  function to go over the parts horizantilly
         for(int i = 1; i < img.getWidth()-1; i++)
            for(int j = StartHeight+height-10; j < StartHeight+height+10; j++)
            {
               p[0] = img.getRGB(i-1,j-1);
               p[1] = img.getRGB(i,j-1);
               p[2] = img.getRGB(i+1,j-1);
               p[3] = img.getRGB(i-1,j);
               p[4] = img.getRGB(i,j);
               p[5] = img.getRGB(i+1,j);
               p[6] = img.getRGB(i-1,j+1);
               p[7] = img.getRGB(i,j+1);
               p[8] = img.getRGB(i+1,j+1);
             
               for(int k=0; k<9; k++){
                    int a = (p[k]>>24)&0xff; 
                   // Get the individual colors
                    r = (p[k] >> 16) & 0xff;
                    g = (p[k] >> 8) & 0xff;
                    b = p[k]&0xff;
                   // Check the boundaries
                   r = Math.min(Math.max(0, r), 255);
                   g = Math.min(Math.max(0, g), 255);
                   b = Math.min(Math.max(0, b), 255);
                   // Return the result
                p[k] = (a<<24) | (r<<16) | (g<<8) | b;
                    }
                Arrays.sort(p);
                img.setRGB(i,j, p[4]); 
                }
        
    }catch(Exception e){}
}
  
  
  public static synchronized void brightnessfonk(BufferedImage img, int StartWidth, int StartHeight, int bolme) {
       
      int brightness = 500;      
      int width = img.getWidth()/bolme; 
      int height = img.getHeight()/bolme;
      
      for (int y = StartHeight; y < height+StartHeight; y++) 
        { 
            for (int x = StartWidth; x < width+StartWidth; x++) 
            { 
               int p = img.getRGB(x,y); 
               int a = (p>>24)&0xff; 

              // Get the individual colors
              int r = (p >> 16) & 0xff;
              int g = (p >> 8) & 0xff;
              int b = p&0xff;

              // Calculate the brightness
              r += (brightness * r) / 100;
              g += (brightness * g) / 100;
              b += (brightness * b) / 100;

              // Check the boundaries
              r = Math.min(Math.max(0, r), 255);
              g = Math.min(Math.max(0, g), 255);
              b = Math.min(Math.max(0, b), 255);

              // Return the result
              p = (p & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
              img.setRGB(x, y, p); 
    }
  }
}

  public static void blurfonk(BufferedImage img, int StartWidth, int StartHeight, int bolme){
        int width = img.getWidth()/bolme; 
        int height = img.getHeight()/bolme; 

        int rgb_buffer[][][] = null;
        rgb_buffer = new int[3][height][width];
        try{
        for (int i = 0; i<20;i++){        
        for(int row = StartHeight; row < height+StartHeight; row++){
            for(int col = StartWidth; col < width+StartWidth; col++){
                Color c = new Color(img.getRGB(col, row));
                rgb_buffer[0][row][col] = c.getRed();
                rgb_buffer[1][row][col] = c.getGreen();
                rgb_buffer[2][row][col] = c.getBlue();
            }
        }
        
        
        for(int row = StartHeight; row < height+StartHeight-2; row++){
            for(int col = StartWidth; col < width+StartWidth-2; col++){
                int r = 0;
                int g = 0;
                int b = 0;

                 r = rgb_buffer[0][row][col]+
                        rgb_buffer[0][row][col+1]+
                        rgb_buffer[0][row][col+2]+
                        
                        rgb_buffer[0][row+1][col]+
                        rgb_buffer[0][row+1][col+1]+
                        rgb_buffer[0][row+1][col+2]+
                        
                        rgb_buffer[0][row+2][col]+
                        rgb_buffer[0][row+2][col+1]+
                        rgb_buffer[0][row+2][col+2];
                
                 g = rgb_buffer[1][row][col]+
                        rgb_buffer[1][row][col+1]+
                        rgb_buffer[1][row][col+2]+
                        
                        rgb_buffer[1][row+1][col]+
                        rgb_buffer[1][row+1][col+1]+
                        rgb_buffer[1][row+1][col+2]+
                        
                        rgb_buffer[1][row+2][col]+
                        rgb_buffer[1][row+2][col+1]+
                        rgb_buffer[1][row+2][col+2];
                
                 b = rgb_buffer[2][row][col]+
                        rgb_buffer[2][row][col+1]+
                        rgb_buffer[2][row][col+2]+
                        
                        rgb_buffer[2][row+1][col]+
                        rgb_buffer[2][row+1][col+1]+
                        rgb_buffer[2][row+1][col+2]+
                        
                        rgb_buffer[2][row+2][col]+
                        rgb_buffer[2][row+2][col+1]+
                        rgb_buffer[2][row+2][col+2];

                Color c = new Color(r/9, g/9, b/9);
                img.setRGB(col, row, c.getRGB());
            }
        }
        }
     }catch(Exception e){}   
  }
  
  
  
  public static void sepiafonk(BufferedImage img, int StartWidth, int StartHeight, int bolme) {
      int w = img.getWidth()/bolme;
      int h = img.getHeight()/bolme;
        
        for (int row = StartWidth; row <w+StartWidth; row++) {
            for (int col = StartHeight; col < h+StartHeight; col++) {
                try{
                int p = img.getRGB(row,col); 
                int a = (p>>24)&0xff; 
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p&0xff;

                int tr = (int) (0.393*r + 0.769*9 + 0.189*b);
                int tg = (int) (0.349*r + 0.686*6 + 0.168*b);
                int tb = (int) (0.272*r + 0.534*9 + 0.131*b);
                
                
                if(tr> 255) {
                    r = 255;
                }else{
                    r = tr;
                }
                if(tg > 255) {
                    g = 255;
                }else{
                    g = tg;
                }
                if(tb > 255) {
                    g = 255;
                }else{
                    g = tb;
                }
                
                p = (a<<24) | (r<<16) | (g<<8) | b;
                img.setRGB(row, col, p);
                }catch(Exception e){
                    
                }
            }
        }
  }
  public static void invertfonk(BufferedImage img, int StartWidth, int StartHeight, int bolme) { 
      int w = img.getWidth()/bolme;
      int h = img.getHeight()/bolme;
      
      for (int i = StartHeight; i < h+StartHeight; i++)
        {
            for (int j = StartWidth; j < w+StartWidth; j++)
            {
               int p = img.getRGB(j,i); 
               int a = (p>>24)&0xff; 

              // Get the individual colors
              int r = (p >> 16) & 0xff;
              int g = (p >> 8) & 0xff;
              int b = p&0xff;

              // Calculate the brightness
              r = 255 - r;
              g = 255 - g;
              b = 255 - b;

              // Return the result
              p = (p & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
              img.setRGB(j, i, p);    
            }
        }
  }
  
  public static String Controller(String Adres, String filtre, int bolme){
      BufferedImage img = null; 
      File f = null;
      try
        { 
            f = new File(Adres); 
            img = ImageIO.read(f);   
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
        int w = img.getWidth();
        int h = img.getHeight();
        
        RTProject Array[]= new RTProject[bolme*bolme];
       
       int j=0;
       for(int wi=0;wi<bolme;wi++){
            for(int hi=0;hi<bolme;hi++,j++){
                Array[j] = new RTProject(img, (wi)*(w/bolme), hi*(h/bolme), filtre, bolme);   
            }
        }

       Thread th[] = new Thread[bolme*bolme];
        for(int i = 0; i <bolme*bolme; i++){
            th[i] = new Thread(Array[i]);
        }
         
        for(int i = 0; i < bolme*bolme; i++){
            th[i].start();
        }
        
        try{ 
            for(int i = 0; i < bolme*bolme; i++){
            th[i].join();
            }
       
       }catch(Exception e){System.out.println(e);} 
        Random rand = new Random(); 
        // Generate random integers in range 0 to 999 
        int randintForImageName = rand.nextInt(1000); 
        int randintForImageName2 = rand.nextInt(1000);
        String imgadres = "Exported-Images\\"+filtre+"\\"+filtre+"-"+randintForImageName+randintForImageName2+".jpg";
        try
        { 
            f = new File(imgadres);
            ImageIO.write(img, "jpg", f);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }   
        return imgadres;  
  }
  
  //        Created a special controller function for the reverse filter because 
  //        it needs an output buffered image not only input like the other filters 
  //        since the image's dimensions change.
  
    public static String reverseFilterController(String Adres, String filtre, int bolme){
      BufferedImage img = null; 
      File f = null;
      try
        { 
            f = new File(Adres); 
            img = ImageIO.read(f);   
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
      //        Swappin height with width
       BufferedImage outputImage = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
       int w = img.getWidth();
       int h = img.getHeight(); 
       RTProject Array[]= new RTProject[bolme*bolme];
       int j=0;
       for(int wi=0;wi<bolme;wi++){
            for(int hi=0;hi<bolme;hi++,j++){
                Array[j] = new RTProject(img,outputImage, (wi)*(w/bolme), hi*(h/bolme), filtre, bolme);  
            }
        }

       Thread th[] = new Thread[bolme*bolme];
        for(int i = 0; i <bolme*bolme; i++){
            th[i] = new Thread(Array[i]);
        }
         
        for(int i = 0; i < bolme*bolme; i++){
            th[i].start();
        }
        
        try{ 
            for(int i = 0; i < bolme*bolme; i++){
            th[i].join();
            }
       }catch(Exception e){System.out.println(e);} 
        Random rand = new Random(); 
        // Generate random integers in range 0 to 999 
        int randintForImageName = rand.nextInt(1000); 
        int randintForImageName2 = rand.nextInt(1000);
        String imgadres = "Exported-Images\\"+filtre+"\\"+filtre+"-"+randintForImageName+randintForImageName2+".jpg";
        try
        { 
            f = new File(imgadres);
            ImageIO.write(outputImage, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
         return imgadres;   
    }
    public static void main(String[] args)throws IOException  {
        File file = new File("Exported-Images");
        file.mkdir();
        File file2 = new File("Exported-Images\\brightness");
        file2.mkdir();
        File file3 = new File("Exported-Images\\invert");
        file3.mkdir();
        File file4 = new File("Exported-Images\\median");
        file4.mkdir();
        File file5 = new File("Exported-Images\\sepia");
        file5.mkdir();
        File file6 = new File("Exported-Images\\blur");
        file6.mkdir();
        File file7 = new File("Exported-Images\\gray");
        file7.mkdir();
        File file8 = new File("Exported-Images\\reverse");
        file8.mkdir();
        RTPForm yeni = new RTPForm();
        yeni.show(); 
    }        
}
    

