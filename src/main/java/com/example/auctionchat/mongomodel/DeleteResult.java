package com.example.auctionchat.mongomodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteResult {

    private boolean acknowledged;
    private Long deletedCount;
}
