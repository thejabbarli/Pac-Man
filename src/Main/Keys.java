package Main;

import Entity.AllDirections;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static Entity.AllDirections.*;

public class Keys implements KeyListener {

    private AllDirections currentDirection = AllDirections.NULL;
    private AllDirections queuedDirection = AllDirections.NULL;

    public Keys() {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // NOT USED
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyPressed = e.getKeyCode();

        switch (keyPressed) {

            case KeyEvent.VK_W:
                queuedDirection = UP;
                break;
            case KeyEvent.VK_A:
                queuedDirection = LEFT;
                break;
            case KeyEvent.VK_S:
                queuedDirection = DOWN;
                break;
            case KeyEvent.VK_D:
                queuedDirection = RIGHT;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public AllDirections getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(AllDirections currentDirection) {
        this.currentDirection = currentDirection;
    }

    public AllDirections getQueuedDirection() {
        return queuedDirection;
    }

    public void setQueuedDirection(AllDirections queuedDirection) {
        this.queuedDirection = queuedDirection;
    }
}
