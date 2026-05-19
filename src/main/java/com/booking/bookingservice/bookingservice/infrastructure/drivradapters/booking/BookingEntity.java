package com.booking.bookingservice.bookingservice.infrastructure.drivradapters.booking;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("booking")
@Entity
public class BookingEntity {

    @Id()
    private UUID id;
    private UUID fkFlight;
    private String userName;
    private String status;
    private int reservationAmount;


    public BookingEntity() {
    }

    public BookingEntity(UUID id, UUID fkFlight, String userName, String status, int reservationAmount) {
        this.id = id;
        this.fkFlight = fkFlight;
        this.userName = userName;
        this.status = status;
        this.reservationAmount = reservationAmount;
    }

    public BookingEntity(UUID id, UUID fkFlight, String userName) {
        this.id = id;
        this.fkFlight = fkFlight;
        this.userName = userName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFkFlight() {
        return fkFlight;
    }

    public void setFkFlight(UUID fkFlight) {
        this.fkFlight = fkFlight;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReservationAmount() {
        return reservationAmount;
    }

    public void setReservationAmount(int reservationAmount) {
        this.reservationAmount = reservationAmount;
    }
}
