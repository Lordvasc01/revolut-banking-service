package com.revolut.app;

import com.revolut.infrastructure.exception.InsufficientFundException;
import com.revolut.infrastructure.exception.UnauthorisedTransaction;
import com.revolut.rest.v1.controller.AccountControllerImpl;
import com.revolut.rest.v1.controller.TransferControllerImpl;

import static com.revolut.app.Utils.json;
import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

public class Routes {

    public void handle(AccountControllerImpl accountController, TransferControllerImpl transferController) {
        post("/account", (req, res) -> accountController.create(req.body()), json());
        get("/account/:id", (req, res) -> accountController.getAccountById(req.params(":id")), json());
        get("/account", (req, res) -> accountController.getAllAccount(), json());
        post("/transfer", (req, res) -> transferController.transfer(req.body()), json());
        after((req, res) -> {res.type("application/json");});

        // Exception Handler.
        exception(InsufficientFundException.class, (e, request, response) -> {
            response.status(402);
            response.body("Insufficient found");
        });
        exception(UnauthorisedTransaction.class, (e, request, response) -> {
            response.status(401);
            response.body("Insufficient found");
        });
    }
}