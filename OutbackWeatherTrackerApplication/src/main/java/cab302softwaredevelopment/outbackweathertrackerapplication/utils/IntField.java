package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class IntField extends TextField {
    final private IntegerProperty value;
    final private int minValue;
    final private int maxValue;

    public int  getValue() {
        return value.getValue();
    }
    public void setValue(int newValue) {
        value.setValue(newValue);
    }
    public IntegerProperty valueProperty() {
        return value;
    }

    public IntField(int minValue, int maxValue, int initialValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException(minValue + " is not less than " + maxValue);
        }
        if (!((minValue <= initialValue) && (initialValue <= maxValue))) {
            throw new IllegalArgumentException(initialValue + " is out of range.");
        }

        this.minValue = minValue;
        this.maxValue = maxValue;
        value = new SimpleIntegerProperty(initialValue);
        setText(initialValue + "");

        final IntField intField = this;

        value.addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                intField.setText("");
            } else {
                if (newValue.intValue() < intField.minValue) {
                    value.setValue(intField.minValue);
                    return;
                }

                if (newValue.intValue() > intField.maxValue) {
                    value.setValue(intField.maxValue);
                    return;
                }

                if (!(newValue.intValue() == 0 && (textProperty().get() == null || "".equals(textProperty().get())))) {
                    intField.setText(newValue.toString());
                }
            }
        });

        this.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if(intField.minValue<0) {
                if (!"-0123456789".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            } else if (!"0123456789".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
            }
        });

        this.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty() || (intField.minValue<0 && "-".equals(newValue))) {
                value.setValue(0);
                return;
            }

            final int intValue = Integer.parseInt(newValue);

            if (intField.minValue > intValue || intValue > intField.maxValue) {
                textProperty().setValue(oldValue);
            }

            value.set(Integer.parseInt(textProperty().get()));
        });
    }
}