package langtons.ant;

import android.graphics.* ;
import android.util.DisplayMetrics;
import android.view.* ;
import android.content.Context ;
import android.util.AttributeSet ;

public class LangtonsAntView extends View implements Runnable {

    //Paints for drawing cells
    Paint gridPaint = new Paint();
    Paint paintItBlack = new Paint();
    Paint antPaint = new Paint();

    //These are used to calculate the size of the grid somewhat dynamically
    float screenWidth = 0.0f;
    float screenHeight = 0.0f;
    float screenHeightDpi = 0.0f;
    float magicNumber = 0.0f;
    float availableHeight = 0.0f;
    int gridCellsX = 0;
    int gridCellsY = 0;

    WindowManager wm;
    Display display;
    Point size;
    DisplayMetrics metrics;

    //boolean variables used to check if specific actions are taking place
    boolean gridBeingSet = false;
    boolean antBeingSet = false;
    boolean simulationRunning = false;

    /*boolean array to store whether a specific cell in the grid should be black or white
      black = true, white = false
     */
    boolean[][] simulationGrid;
    boolean firstCycle = true;

    int antY, antX; // ant position
    int antChangeX = 0, antChangeY = -1; //These tell the ant which direction to move. By default the first move is up
    Thread simulationThread;

    public LangtonsAntView( Context context )
    {
        super( context ) ;
    }

    public LangtonsAntView( Context context, AttributeSet attributes )
    {
        super( context, attributes );
    }

    public void onSizeChanged( int current_width_of_this_view,
                               int current_height_of_this_view,
                               int old_width_of_this_view,
                               int old_height_of_this_view )
    {

        gridPaint.setStyle(Paint.Style.STROKE); //This paint draws white cells with black borders
        paintItBlack.setStyle(Paint.Style.FILL_AND_STROKE); //This paint draws completely black cells;

        antPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        antPaint.setColor(Color.RED);

        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        display =  wm.getDefaultDisplay();

        size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        /*
            We want the cells to be 40x40 pixels. Removing two from the total amount of X-axis cells
            makes the grid look more satisfying (while emulating on Nexus 5X at least).
         */

        gridCellsX = ((int) screenWidth / 40) - 2;


        metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        screenHeightDpi = metrics.ydpi;
        magicNumber = 60 / screenHeightDpi;

        /*
            60 = static height of the layout that holds the buttons
            I'm sure there's a better way to do all this
            so I might return to this when I get better at Android programming
         */

        availableHeight = screenHeight - (screenHeight * magicNumber);
        gridCellsY = ((int) availableHeight / 40);
        simulationGrid = new boolean[gridCellsY][gridCellsX];
    }

    public void enableGridSetting(){
        gridBeingSet = true;
    }

    public void disableGridSetting(){
        gridBeingSet = false;
    }

    public void enableAntSetting(){
        antBeingSet = true;
    }

    public void disableAntSetting(){
        antBeingSet = false;
    }

    public void startSimulation(){
        if(simulationThread == null) {
            simulationThread = new Thread(this);
            simulationRunning = true;
            simulationThread.start();
        }
    }

    public void endSimulation(){
        if(simulationThread != null) {
            simulationThread.interrupt();
            simulationRunning = false;
            simulationThread = null;
        }
    }

    @Override
    public void run() {

        while(simulationRunning){
            //turn left if the ant's current position is black
            if(simulationGrid[antY][antX]){
                //moving up or down
                if(antChangeX == 0){
                    antChangeX = antChangeY;
                    antChangeY = 0;
                //moving left or right
                }else{
                    antChangeY = -antChangeX;
                    antChangeX = 0;
                }
            //turn right if the ant's current position is white
            }else{
                if(antChangeX == 0){
                    antChangeX = -antChangeY;
                    antChangeY = 0;
                    //moving left or right
                }else{
                    antChangeY = antChangeX;
                    antChangeX = 0;
                }
            }
            simulationGrid[antY][antX] = !simulationGrid[antY][antX];
            antX += antChangeX;
            antY += antChangeY;

            /*Let's check if the ant is about to enter outside the grid
              and tell it to come out on the opposite instead
             */

            if(antX < 0){
                antX = gridCellsX - 1;
            }else if(antX > gridCellsX - 1){
                antX = 0;
            }

            if(antY < 0){
                antY = gridCellsY - 1;
            }else if(antY > gridCellsY - 1){
                antY = 0;
            }

            postInvalidate();
            try{
                Thread.sleep(100);
            }catch(InterruptedException ex){
                simulationRunning = false;
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas){

        if(firstCycle) {

            //By default the ant is set to the center point of the grid
            antY = (int) (gridCellsY / 2);
            antX = (int) (gridCellsX / 2);
            firstCycle = false;
        }

        for(int i = 0; i < gridCellsY; i++){
            int yPlacement = (i + 1) * 40;

            for(int j = 0; j < gridCellsX; j++){
                int xPlacement = (j + 1) * 40;
                if(i == antY && j == antX) {
                    canvas.drawRect(xPlacement, yPlacement, xPlacement + 40, yPlacement + 40, antPaint);
                }else if(!simulationGrid[i][j]) {
                    canvas.drawRect(xPlacement, yPlacement, xPlacement + 40, yPlacement + 40, gridPaint);
                }else{
                    canvas.drawRect(xPlacement, yPlacement, xPlacement + 40, yPlacement + 40, paintItBlack);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(gridBeingSet) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                /*
                We get the coordinates of a specific clicked cell
                by dividing the coordinates where the event originated
                with the length of the cell's side
                 */
                try{
                    int y = (int) (event.getY() / 40) - 1;
                    int x = (int) (event.getX() / 40) - 1;
                    simulationGrid[y][x] = !simulationGrid[y][x];
                    invalidate();
                }catch(java.lang.ArrayIndexOutOfBoundsException ex){
                    return true;
                }
            }
        }
        if(antBeingSet) {

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                /*
                    We set the ant just like we set the color of grid cells,
                    except there can be only one ant and the ant can't be placed
                    outside of the grid.
                 */
                antX = (int) (event.getX() / 40) - 1;
                if(antX < 0){
                    antX = 0;
                }else if(antX > gridCellsX - 1){
                    antX = gridCellsX - 1;
                }

                antY = (int) (event.getY() / 40) - 1;
                if(antY < 0){
                    antY = 0;
                }else if(antY > gridCellsY - 1){
                    antY = gridCellsY - 1;
                }

                invalidate();
            }
        }
        return true;
    }
}
