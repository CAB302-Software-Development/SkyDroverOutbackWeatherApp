package com.example;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Running Preds...");

        final int number_of_rows = 28;
        final int number_of_colums = 28;

        int output_number = 10;
        int batch_size = 10;
        int rng_seed = 123;
        int number_of_ephocs = 10;

        float learning_rate = 0.01f;

        DataSetIterator mnist_train = new MnistDataSetIterator(batch_size, true, rng_seed);
        DataSetIterator mnis_test = new MnistDataSetIterator(batch_size, false, rng_seed);

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(rng_seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(learning_rate))
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(number_of_rows * number_of_colums)
                        .nOut(1000)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(1000)
                        .nOut(output_number)
                        .activation(Activation.SOFTMAX)
                        .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);

        model.init();
        model.setListeners(new ScoreIterationListener(10));

        for(int i = 0; i < number_of_ephocs; i++) {
            model.fit(mnist_train);
        }

        Evaluation eval = new Evaluation(output_number);

        while(mnis_test.hasNext()) {
            DataSet next = mnis_test.next();
            INDArray output = model.output(next.getFeatures());

            eval.eval(next.getLabels(), output);
        }

        System.out.println(eval.stats());
    }
}