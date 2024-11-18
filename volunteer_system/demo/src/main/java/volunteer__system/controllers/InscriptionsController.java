package volunteer__system.controllers;

import volunteer__system.entities.Inscription;
import volunteer__system.models.interfaces.IInscriptionsModel;

public class InscriptionsController {
    private final IInscriptionsModel inscriptionsModel;

    public InscriptionsController(IInscriptionsModel inscriptionsModel) {
        this.inscriptionsModel = inscriptionsModel;
    }

    public Boolean create(Inscription inscription) {
        return this.inscriptionsModel.create(inscription);
    }

}
