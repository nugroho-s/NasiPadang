/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubesduaai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.NativeArray.map;
import static jdk.nashorn.internal.objects.NativeDebug.map;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.NumericToNominal;


/**
 *
 * @author harry
 */
public class NB_030 extends AbstractClassifier implements Classifier, Serializable {
    
    public Instances datas;
    public Instances data_test;
    public double[][] temp;
    public HashMap<String, Float> map;
    public String[][] string;
    public Float[] num;
    public Integer NUM_LABELS;
    
    public NB_030() {
        map = new HashMap<String, Float>();
    }   
    
    public void save_model() {
        try {
            
            FileWriter file = new FileWriter("coba2.txt");
            BufferedWriter bw = new BufferedWriter(file);
            for (int j=0; j < datas.numAttributes(); j++) {
            if (j != datas.classIndex()) {
                String[] distincts = distinctVals(j);
                System.out.println(datas.attribute(j).name());
                    for (int m=0; m < distincts.length; m++) {
                        for (int i=0; i < NUM_LABELS; i++) {
                            bw.write(datas.attribute(j).name()+distincts[m]+datas.classAttribute().value(i));
                            bw.write(" ");
                            bw.write(map.get(datas.attribute(j).name()+distincts[m]+datas.classAttribute().value(i)).toString());
                            bw.write("\n");
                        }
                    }
                }
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void load_model(String path) {
        
        try {
//            FileReader file = new FileReader(path);
//            FileReader file = new FileReader("coba2.txt");
            BufferedReader br = new BufferedReader(new FileReader(path));
            String temp;
            while ((temp = br.readLine()) != null){
                String[] part = temp.split(" ");
                // pasti 2
                map.put(part[0], Float.parseFloat(part[1]));
                System.out.println(part[0] + " , " + map.get(part[0]));
            }
//            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void Discretize(String instncs) throws Exception {
        NumericToNominal discretize = new NumericToNominal();
        String[] options = new String[6];
//        options[0] = "-R";                                    
//        options[1] = "first-last";
//        options[2] = "-precision";
//        options[3] = "6";
/////////////////////////////////////
//        options[0] = "-B";
//        options[1] = "10";
//        options[2] = "-M";
//        options[3] = "-1.0";
//        options[4] = "-R";
//        options[5] = "first-last";
//        discretize.setOptions(options);
/////////////////////////////////////////
//        String[] options = new String[1];
//        options[0] = "-o";
//        options[1] = "6";
//        options[2] = "-precision";
//        options[3] = "6";
//        discretize.setOptions(options);
//        discretize.setUseBetterEncoding(true);
//        System.out.println(discretize.useBinNumbersTipText());
        if (instncs.equals("datas")) {
            discretize.setInputFormat(datas);
            datas = Filter.useFilter(datas,discretize);
        } else if (instncs.equals("data_test")) {
            discretize.setInputFormat(data_test);
            data_test = Filter.useFilter(data_test,discretize);
        }
        
    }
    
    public void DataRead(String filepath, int index, int test) throws Exception {
        if (test == 0) {
            datas = DataSource.read(filepath);
            datas.setClassIndex(index);

    //        datas.setClassIndex(datas.numAttributes()-1);
            string = new String[datas.numAttributes()][];
            NUM_LABELS = datas.numClasses();
        } else {
            data_test = DataSource.read(filepath);
            data_test.setClassIndex(index);
            Discretize("data_test");
        }
    }
        
    public String[] distinctVals(int index) {
        int nums = datas.numDistinctValues(index);
        String[] dist = new String[nums];
        int j = 0;
        for (int i=0; i < datas.numInstances(); i++) {
            if (!Arrays.asList(dist).contains(datas.get(i).stringValue(index))) {
                dist[j] = datas.get(i).stringValue(index);
                j++;
            }
        }
        return dist;
    }
    
    @Override
    /**
     * Make model
     */
    public void buildClassifier(Instances inst) throws Exception {
        temp = new double[datas.numInstances()][];
        Discretize("datas");
                        
        // INSERT DATA KE STRUKTUR DATA
//        for (int j=0; j < datas.numAttributes()-1; j++) {
        for (int j=0; j < datas.numAttributes(); j++) {
            if (j != datas.classIndex()) {
                String[] distincts = distinctVals(j);
//                System.out.println(datas.get(j).attribute(j));
                for (int m=0; m < distincts.length; m++) {
                    for (int i=0; i < NUM_LABELS; i++) {
                        map.put(datas.attribute(j).name()+distincts[m]+datas.classAttribute().value(i), new Float(1));
                    }
                }
            }
        }
//        System.out.println("atribute 2 : " + datas.attribute(2).toString());
////DEBUGGING : /////////FOR DEBUG PURPOSE///////////////////////////LOOOOOOOOPPPP>????????//////////////////////        
//        System.out.println("Before : ");
//        System.out.println("==============================================");
////        for (int j=0; j < datas.numAttributes()-1; j++) {
//        for (int j=0; j < datas.numAttributes(); j++) {
//            if (j != datas.classIndex()) {
//                String[] distincts = distinctVals(j);
//                System.out.println(datas.attribute(j).name());
//                for (int m=0; m < distincts.length; m++) {
//                    for (int i=0; i < NUM_LABELS; i++) {
//                        System.out.print(map.get(datas.attribute(j).name()+distincts[m]+datas.classAttribute().value(i)));
//                        System.out.print(" ");
//                    }
//                    System.out.println();
//                }
//            }
//        }
        System.out.println("====================================================");
////DEBUGGING : ///////////////////////////FOR DEBUG PURPOSE///////////////////////////////////////
//        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//        System.out.println(datas.get(0).attribute(0).name());
//        System.out.println(datas.attribute(1).name());
//        System.out.println(datas.attribute(2).name());
//        System.out.println(datas.attribute(3).name());
//        System.out.println(datas.attribute(4).name());

///////////////////////////////MAPPING//////////////////////////////////////////////
        num = new Float[datas.numClasses()];
        for (int j=0; j < datas.numClasses(); j++) {
            num[j] = new Float(0);
        }
//        System.out.println("mapping "+ datas.classAttribute().value(0));
//        System.out.println("mapping "+ datas.classAttribute().value(1));
//        System.out.println("mapping "+ datas.classAttribute().value(2));
        for (int k=0; k < datas.numInstances(); k++) {
            for (int i=0; i < NUM_LABELS; i++) {
                if (datas.get(k).stringValue(datas.classIndex()).equals(datas.classAttribute().value(i))) {
                    num[i] += 1;
                    break;
                }
            }
        }
        Float y;
        for (int j=0; j < datas.numAttributes(); j++) {
            if (j != datas.classIndex()) {
                String[] distincts = distinctVals(j);
                string[j] = distincts;
                for (int k=0; k < datas.numInstances(); k++) {
                    for (int m=0; m < distincts.length; m++) {
                        if (datas.get(k).stringValue(j) == distincts[m]) {
                            for (int i=0; i < NUM_LABELS; i++) {
                                if (datas.get(k).stringValue(datas.classAttribute()) == datas.classAttribute().value(i)) {

                                    Float x = map.get(datas.get(k).attribute(j).name()+
                                            distincts[m]+datas.classAttribute().value(i))+ 1;

                                    map.put(datas.get(k).attribute(j).name()+distincts[m]
                                            +datas.classAttribute().value(i), x);

                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (int j=0; j < datas.numAttributes(); j++) {
            if (j != datas.classIndex()) {
                String[] distincts = distinctVals(j);
//                System.out.println(datas.attribute(j).name());
                for (int m=0; m < distincts.length; m++) {
                    for (int i=0; i < NUM_LABELS; i++) {
    //                  System.out.print(map.get(datas.attribute(j).name()+distincts[m]+datas.classAttribute().value(0)));
                        y = map.get(datas.attribute(j).name()+distincts[m]+datas.classAttribute().value(i));
//                        FOR DEBUG PURPOSE
//                        System.out.println(y/num[i]);
                        ///////////////////////////////
                        map.put(datas.attribute(j).name()+distincts[m]+datas.classAttribute().value(i), y/num[i]);
                    }
                }
            }
        }

///////FOR DEBUG PURPOSE//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        System.out.println("somesome");
//        System.out.println(num[0]);
//        System.out.println(num[1]);
//        System.out.println(num[2]);
///////////////////////////CALCULATING ACCURACY///////////////////////////////////////////////
//        for (int i=0; i < datas.numInstances(); i++) {
//            float[] argmax = new float[3];
//            for (int q=0; q < datas.numClasses(); q++) {
//                float prob = 0;
//                
//            }
//        }

///////FOR DEBUG PURPOSE////////////////////////////////////////////////////////////////////////////////////////////
/////DEBUGGING : ////////////////////////////////ENDDOFMAPPING//////////////////////////////////////
//        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//        System.out.println("After : ");
//        System.out.println("==========================");
//        for (int j=0; j < datas.numAttributes(); j++) {
//            if (j != datas.classIndex()) {
//                String[] distincts = distinctVals(j);
//                System.out.println(datas.attribute(j).name());
//                for (int m=0; m < distincts.length; m++) {
//                    for (int i=0; i < NUM_LABELS; i++) {
//                        System.out.print(datas.attribute(j).name()+distincts[m]+datas.classAttribute().value(i) +  " ");
//                        System.out.print(map.get(datas.attribute(j).name()+distincts[m]+datas.classAttribute().value(i)));
//                        System.out.print(" ");
//                    }
//                    System.out.println();
//                }
//            }
//        }
////////FOR DEBUG PURPOSE/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////// CALCULATE ACCURACY AND CLASSIFY ACCURACY //////////////////////////////////
        /////////////////MEMASUKKAN KELAS MENURUT MODEL KE ARRAY
//        Double[] hasilTraining = new Double[datas.numInstances()];
//        for (int i=0; i <  datas.numInstances(); i++) {
//            /// CALCULATION OF PROBABILITY
//            hasilTraining[i] = 1.0;
//            /// KEYS UNTUK KE MAP
//            double current_argmax;
//            for (int k=0; k < datas.numClasses(); k++) {
//                current_argmax = 0.0;
//                for (int j=0; j  < datas.numAttributes()-1; j++) {
//                    String atribut = datas.attribute(j).name();
//                    System.out.println("atribut " + atribut);
//                    String value = datas.get(i).stringValue(j);
//                    System.out.println("value " + value);
//                    String kelas = datas.classAttribute().value(k);
//                    System.out.println("kelas " + kelas);
//                    hasilTraining[i] *= map.get(atribut+value+kelas);
//                }
//                if (current_argmax < hasilTraining[i]) current_argmax = hasilTraining[i];
//            }
//            
////            hasilTraining[i] *= ;
//            System.out.println("hasil training " + i + " : " + hasilTraining[i]);
//        }
        ////////////////////
//        for (int i=0; i < datas.numInstances(); i++) {
////            if (datas.get(i).stringValue(datas.classIndex()) == )
//        }
//       System.out.println("keluar method");
//////END OF METHOD////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public double classifyInstance(Instance instnc) throws Exception {
        // Pengklasifikasi instance baru
        System.out.println("instnc : " + instnc.toString());
        int NUM_CLASSES = datas.classAttribute().numValues();
//        System.out.println("------------------------------");
//        System.out.println(datas.get(1));
//        System.out.println(datas.get(1).stringValue(2));
        // Probability of classes
        int arg = 0;
        Double argmax = 0.0;
        Double temp = 1.0;
        for (int i=0; i < NUM_CLASSES; i++) { // arg max (vj E enum.attributes(datas))
            // P(kelas)*P(atribut|kelas)
//            System.out.println(i);
            temp = ((new Double(num[i]))/datas.numInstances());
            for (int j=0; j < datas.numAttributes(); j++) {
                if (j != datas.classIndex()) {
//                    System.out.println("temp = "+ temp + " ; i = " + i + " ; j = " + j + " ; name = " + instnc.attribute(30).name() + " ; value = " + instnc.stringValue(30));
//                    System.out.println(" ; debug : " + instnc.classAttribute().value(1));
////                    System.out.println("i = " + i);
////                    System.out.println("j = " + j);
////                    System.out.println("--------------");
////                    System.out.println(map.get(instnc.attribute(j).name()+instnc.stringValue(j)+datas.classAttribute().value(i)));
////                    System.out.println("--------------");
////                    System.out.println(datas.classAttribute().value(i));
//                    System.out.print("instnc.attribute("+j+").name()+instnc.stringValue("+j+")+instnc.classAttribute().value("+i + ") ");
                    System.out.println(instnc.attribute(j).name()+instnc.stringValue(j)+instnc.classAttribute().value(i));
//                    System.out.println(map.get(instnc.attribute(j).name()+instnc.stringValue(j)+instnc.classAttribute().value(i)));
                    
                    temp *= map.get(instnc.attribute(j).name()+instnc.stringValue(j)+instnc.classAttribute().value(i));
                }
            }
            if (temp > argmax) {
                argmax = temp;
                arg = i;
            }
        }
        switch (arg) {
            case 0 :
                return 0.0;
            case 1 :
                return 1.0;
            case 2 :
                return 2.0;
            default:
                return 0.0;
        }
    }

    @Override
    public double[] distributionForInstance(Instance instnc) throws Exception {
        double temp = classifyInstance(instnc);
        int x = datas.classAttribute().numValues();
        double[] ret = new double[x];
        
        for (int i=0;i<x;i++){
            ret[i] = (1-0.5)/(x-1);
        }
        ret[(int) temp] = 0.7;
        return ret;
    }

    @Override
    public Capabilities getCapabilities() {
        return super.getCapabilities();
//        Capabilities result = 
//        Capabilities result = super.getCapabilities();
//        result.disableAll();
//
//        // attributes
//        result.enable(Capabilities.Capability.NOMINAL_ATTRIBUTES);
//        result.enable(Capabilities.Capability.NUMERIC_ATTRIBUTES);
//        result.enable( Capabilities.Capability.MISSING_VALUES );
//
//        // class
//        result.enable(Capabilities.Capability.NOMINAL_CLASS);
//        result.enable(Capabilities.Capability.MISSING_CLASS_VALUES);
//
//        // instances
//        result.setMinimumNumberInstances(0);
//
//        return result;
//        return null;
    }
}
