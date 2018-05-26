package pl.kurcaba;

public enum RequiredAction {
    DROP(0),MOVE(1),HIT(3);

    final int actionNumber;

    RequiredAction(int actionNumber) {

        this.actionNumber = actionNumber;

    }
}
