package com.huhu.swagger2.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "User model")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated User ID")
    private Long id;

    @ApiModelProperty(notes = "first name" , example = "huy")
    private String firstName;

    @ApiModelProperty(notes = "last name" , example = "trinh van")
    private String lastName;

    @ApiModelProperty(notes = "email " , example = "huy8895@gmail.com")
    private String email;
}
