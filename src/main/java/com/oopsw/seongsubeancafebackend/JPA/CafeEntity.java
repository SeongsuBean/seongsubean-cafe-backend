package com.oopsw.seongsubeancafebackend.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "cafe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CafeEntity {

  @jakarta.persistence.Id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cafeId;

  @Column(name = "cafe_name", nullable = false, length = 100)
  private String cafeName;

  @Column(name = "cafe_address", nullable = false, length = 255)
  private String cafeAddress;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @Column(name = "cafe_introduction", columnDefinition = "TEXT")
  private String cafeIntroduction;

  @Column(name = "total_rating", precision = 3, scale = 2)
  private Double totalRating;

  @Column(name = "status", nullable = false)
  private Boolean status = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}