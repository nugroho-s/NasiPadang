/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubesduaai;

import java.io.BufferedReader;
import java.io.FileReader;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Nugroho Satriyanto <massatriya@gmail.com>
 */
public class TesFFNN {

    public static Instances data;

    public static void tes() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Program Files\\Weka-3-8\\data\\iris.arff"));
        data = new Instances(reader);
        reader.close();
        // setting class attribute
        data.setClassIndex(data.numAttributes() - 1);
        Instances dummy = null;
        FFNN nn = new FFNN("C:\\Program Files\\Weka-3-8\\data\\iris.arff", 0);
        boolean[] nom = nn.cek_nominal();
        nn.buildClassifier(data);
        nn.print_perceptron();
        double[] attValues1 = {5.1,3.5,1.4,0.2};
        Instance i1 = new DenseInstance(1.0, attValues1);
        double[] attValues2 = {7.0,3.2,4.7,1.4};
        Instance i2 = new DenseInstance(1.0, attValues2);
        double[] attValues3 = {6.3,3.3,6.0,2.5};
        Instance i3 = new DenseInstance(1.0, attValues3);
        i1.setDataset(data);
        i2.setDataset(data);
        i3.setDataset(data);
        //hasil harusnya 0 1 2
        System.out.println(nn.classifyInstance(i1));
        System.out.println(nn.classifyInstance(i2));
        System.out.println(nn.classifyInstance(i3));
    }

    public static void main(String[] args) {
        try {
            tes();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
