package org.rentalpos.services;

import org.rentalpos.entities.Charge;

public interface iChargeService {


    Charge findCharge(String toolType);
}