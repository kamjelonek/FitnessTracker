package com.capgemini.wsb.fitnesstracker.user.internal;

import jakarta.annotation.Nullable;


record UserDtoPart(@Nullable Long Id,
               String firstName,
               String lastName) {
}
