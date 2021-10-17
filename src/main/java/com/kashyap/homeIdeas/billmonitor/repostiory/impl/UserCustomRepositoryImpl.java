package com.kashyap.homeIdeas.billmonitor.repostiory.impl;

import com.kashyap.homeIdeas.billmonitor.repostiory.NoSQLOperations;
import com.kashyap.homeIdeas.billmonitor.repostiory.UserCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    @Autowired
    private NoSQLOperations noSQLOperations;


}
