package langtons.ant;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.widget.Button;

public class LangtonsAntActivity extends Activity {

    LangtonsAntView langtonView;
    Button setGrid;
    Button setAnt;
    Button startSim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_langtons_ant);
        langtonView =  findViewById(R.id.langtons_ant_view);
        setGrid = (Button) findViewById(R.id.setGridButton);
        setAnt = (Button) findViewById(R.id.setAntButton);
        startSim = (Button) findViewById(R.id.simulateButton);
    }

    public void onSetGridClicked(View view){
        if(setGrid.getText().equals("Set Grid")){
            langtonView.enableGridSetting();
            setGrid.setText("Done!");
            setAnt.setEnabled(false);
            startSim.setEnabled(false);
        }else{
            langtonView.disableGridSetting();
            setGrid.setText("Set Grid");
            setAnt.setEnabled(true);
            startSim.setEnabled(true);
        }
    }

    public void onSetAntClicked(View view){
        if(setAnt.getText().equals("Set Ant")){
            langtonView.enableAntSetting();
            setAnt.setText("Done!");
            setGrid.setEnabled(false);
            startSim.setEnabled(false);
        }else{
            langtonView.disableAntSetting();
            setAnt.setText("Set Ant");
            setGrid.setEnabled(true);
            startSim.setEnabled(true);
        }
    }

    public void onSimulateClicked(View view){
        if(startSim.getText().equals("Simulate!")){
            langtonView.startSimulation();
            startSim.setText("Stop!");
            setGrid.setEnabled(false);
            setAnt.setEnabled(false);
        }else{
            langtonView.endSimulation();
            startSim.setText("Simulate!");
            setGrid.setEnabled(true);
            setAnt.setEnabled(true);
        }
    }
}
