package som;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Paul C Peacock <pcp1976@gmail.com>
 */
public final class SOM{

    private int _height;
    private int _width;
    private double _radius;
    private int _total;
    private double _learningRate;
    private Node _nodes[];
    private int _FVSize;
    private int _PVSize;
    private Random _rand;
    private String FVLabels[];
    private String PVLabels[];

    public SOM(int height, int width, int FVSize, int PVSize, int radius,
            double learningRate) {
        FVLabels = new String[FVSize];
        PVLabels = new String[PVSize];
        this.setHeight(height);
        this.setWidth(width);
        if (radius > 0) {
            this.setRadius(radius);
        } else {
            this.setRadius((this.getHeight() + this.getWidth()) / 2);
        }
        this.setTotal(this.getHeight() * this.getWidth());
        this.setLearningRate(learningRate);
        this.setFVSize(FVSize);
        this.setPVSize(PVSize);
        this._nodes = new Node[this.getTotal()];
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                int index = (i) * (this.getWidth()) + j;
                this._nodes[index] = new Node(
                        this.getFVSize(), this.getPVSize(), i, j);
            }
        }
    }

    public SOM(int height, int width, int FVSize, int PVSize, double learningRate) {
        this(height, width, FVSize, PVSize, (height + width) / 2, learningRate);
    }

    public SOM() {
        this(5, 2, 3, 2, 0.005);
    }

    Random getRand() {
        if (_rand == null) {
            _rand = new Random();
        }
        return _rand;
    }

    /**
     * @return the _height
     */
    int getHeight() {
        return _height;
    }

    /**
     * @param height the _height to set
     */
    void setHeight(int height) {
        this._height = height;
    }

    /**
     * @return the _width
     */
    int getWidth() {
        return _width;
    }

    /**
     * @param width the _width to set
     */
    void setWidth(int width) {
        this._width = width;
    }

    /**
     * @return the _radius
     */
    double getRadius() {
        return _radius;
    }

    /**
     * @param radius the _radius to set
     */
    void setRadius(int radius) {
        this._radius = radius;
    }

    /**
     * @return the _total
     */
    int getTotal() {
        return _total;
    }

    /**
     * @param total the _total to set
     */
    void setTotal(int total) {
        this._total = total;
    }

    /**
     * @return the _learningRate
     */
    double getLearningRate() {
        return _learningRate;
    }

    /**
     * @param learningRate the _learningRate to set
     */
    void setLearningRate(double learningRate) {
        this._learningRate = learningRate;
    }

    /**
     * @return the _node
     */
    Node getNode(int index) {
        return _nodes[index];
    }

    /**
     * @param node the _node to set
     */
    void setNode(Node node, int index) {
        this._nodes[index] = node;
    }

    /**
     * @return the _FV_size
     */
    int getFVSize() {
        return _FVSize;
    }

    /**
     * @param FVSize the _FV_size to set
     */
    void setFVSize(int FVSize) {
        this._FVSize = FVSize;
    }

    /**
     * @return the _PV_size
     */
    int getPVSize() {
        return _PVSize;
    }

    /**
     * @param PVSize the _PV_size to set
     */
    void setPVSize(int PVSize) {
        this._PVSize = PVSize;
    }

    private double[][] getDelta(int VSize) {
        double[][] result = new double[this.getTotal()][VSize];
        for (int j = 0; j < this.getTotal() - 1; j++) {
            for (int i = 0; i < VSize; i++) {
                result[j][i] = 0.0;
            }
        }
        return result;
    }

    public void train(int iterations, List<double[][]> trainVector, boolean batchUpdate) {
        double timeConstant = iterations / Math.log(this.getRadius());
        double radiusDecaying;
        double learningRateDecaying;
        double influence;
        double FVDelta[][] = null;
        double PVDelta[][] = null;

        for (int i = 1; i < iterations + 1; i++) {
            if (batchUpdate) {
                FVDelta = this.getDelta(this.getFVSize());
                PVDelta = this.getDelta(this.getPVSize());
            } else {
                Collections.shuffle(trainVector);
            }
            radiusDecaying = this.getRadius()
                    * Math.exp(-1.0 * i / timeConstant);
            learningRateDecaying = this.getLearningRate()
                    * Math.exp(-1.0 * i / timeConstant);

            for (int j = 0; j < trainVector.size(); j++) {
                if (!batchUpdate) {
                    FVDelta = this.getDelta(this.getFVSize());
                    PVDelta = this.getDelta(this.getPVSize());
                }

                double[] input_FV = trainVector.get(j)[0];
                double[] input_PV = trainVector.get(j)[1];
                int best = this.best_match(input_FV);

                for (int k = 0; k < this.getTotal(); k++) {
                    double dist = this.distance(this.getNode(best),
                            this.getNode(k));
                    if (dist < radiusDecaying) {
                        influence = Math.exp((-1.0 * (Math.pow(dist, 2)))
                                / (2 * radiusDecaying * i));

                        double inf_lrd = influence * learningRateDecaying;

                        for (int l = 0; l < this.getFVSize(); l++) {
                            FVDelta[k][l] = FVDelta[k][l] + inf_lrd
                                    * (input_FV[l] - this.getNode(k).getFV(l));
                        }

                        for (int l = 0; l < this.getPVSize(); l++) {
                            PVDelta[k][l] = PVDelta[k][l] + inf_lrd
                                    * (input_PV[l] - this.getNode(k).getPV(l));
                        }

                    }

                }
                if (!batchUpdate) {
                    for (int k = 0; k < this.getTotal(); k++) {
                        for (int l = 0; l < this.getFVSize(); l++) {
                            this.getNode(k).setFV(l,
                                    this.getNode(k).getFV(l) + FVDelta[k][l]);
                        }

                    }
                    for (int k = 0; k < this.getTotal(); k++) {
                        for (int l = 0; l < this.getPVSize(); l++) {
                            this.getNode(k).setPV(l,
                                    this.getNode(k).getPV(l) + PVDelta[k][l]);
                        }

                    }
                }
            }
            if (batchUpdate) {
                for (int k = 0; k < this.getTotal(); k++) {
                    for (int l = 0; l < this.getFVSize(); l++) {
                        this.getNode(k).setFV(l,
                                this.getNode(k).getFV(l) + FVDelta[k][l]);
                    }

                }
                for (int k = 0; k < this.getTotal(); k++) {
                    for (int l = 0; l < this.getPVSize(); l++) {
                        this.getNode(k).setPV(l,
                                this.getNode(k).getPV(l) + PVDelta[k][l]);
                    }

                }
            }
        }

    }

    public void train(List<double[][]> trainVector) {
        this.train(1000, trainVector);
    }

    public void train(int iterations, List<double[][]> trainVector) {
        this.train(iterations, trainVector, true);
    }

    double distance(Node node1, Node node2) {
        return Math.sqrt(Math.pow((node1.getX() - node2.getX()), 2)
                + Math.pow((node1.getY() - node2.getY()), 2));
    }

    double FV_distance(double[] FV_1, double[] FV_2) {
        double temp = 0.0;
        for (int j = 0; j < this.getFVSize(); j++) {
            temp += Math.pow((FV_1[j] - FV_2[j]), 2);
        }
        temp = Math.sqrt(temp);
        return temp;
    }

    public int best_match(double[] target_FV) {
        double minimum = Math.sqrt(this.getFVSize());
        int minimum_index = 1;
        for (int i = 0; i < this.getTotal(); i++) {
            double temp = this.FV_distance(this.getNode(i).getFV(), target_FV);
            if (temp < minimum) {
                minimum = temp;
                minimum_index = i;
            }
        }
        return minimum_index;
    }

    public double[] predict(double[] FV) {
        int best = this.best_match(FV);
        return this.getNode(best).getPV();
    }

    public String getFVLabel(int index) {
        return FVLabels[index];
    }

    public void setFVLabels(String FVLabel, int index) {
        this.FVLabels[index] = FVLabel;
    }

    public String getPVLabel(int index) {
        return PVLabels[index];
    }

    public void setPVLabel(String PVLabel, int index) {
        this.PVLabels[index] = PVLabel;
    }

    public String[] getFVLabels() {
        return FVLabels;
    }

    void setFVLabels(String[] FVLabels) {
        this.FVLabels = FVLabels;
    }

    public String[] getPVLabels() {
        return PVLabels;
    }

    void setPVLabels(String[] PVLabels) {
        this.PVLabels = PVLabels;
    }
}
