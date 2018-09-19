package kdo.test;


import kdo.mining.CAR;
import kdo.mining.MRMR;
import kdo.mining.feature.DrivingFeature;
import kdo.mining.feature.FeatureMetric;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nozomihitomi
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //setup logger
        Level level = Level.ALL;
        Logger.getGlobal().setLevel(level);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
        Logger.getGlobal().addHandler(handler);
        
        ArrayList<Boolean> behavioral = new ArrayList<>();
        ArrayList<int[]> attributes = new ArrayList<>();

        File file = new File("/Users/nozomihitomi/Dropbox/conMOP/dataset.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                String[] str = line.split(",");
                if (Integer.parseInt(str[0]) == 1) {
                    behavioral.add(true);
                }else{
                    behavioral.add(false);
                }

                int[] attrb = new int[str.length - 1];
                for (int i = 1; i < str.length; i++) {
                    attrb[i - 1] = Integer.parseInt(str[i]);
                }
                attributes.add(attrb);
                line = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

        int[][] dataset = new int[attributes.size()][attributes.get(0).length];
        BitSet behavioralSet = new BitSet(behavioral.size());

        for (int i = 0; i < behavioral.size(); i++) {
            dataset[i] = attributes.get(i);
            behavioralSet.set(i, behavioral.get(i));
        }

        CAR car = new CAR(dataset, true);
        car.run(behavioralSet, 0.1, 0.41, 10);
        List<DrivingFeature> top = car.getTopFeatures(Integer.MAX_VALUE, FeatureMetric.FCONFIDENCE);

        List<DrivingFeature> res = MRMR.minRedundancyMaxRelevance(attributes.size(), behavioralSet, top, 4);
        for (DrivingFeature feat : res) {
            System.out.println(feat);
        }
    }

}
