// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloud.api.ApiConstants;
import com.cloud.api.BaseListAccountResourcesCmd;
import com.cloud.api.IdentityMapper;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.UserResponse;
import com.cloud.user.UserAccount;
import com.cloud.utils.Pair;

@Implementation(description="Lists user accounts", responseObject=UserResponse.class)
public class ListUsersCmd extends BaseListAccountResourcesCmd {
    public static final Logger s_logger = Logger.getLogger(ListUsersCmd.class.getName());

    private static final String s_name = "listusersresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.ACCOUNT_TYPE, type=CommandType.LONG, description="List users by account type. Valid types include admin, domain-admin, read-only-admin, or user.")
    private Long accountType;

    @IdentityMapper(entityTableName="user")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, description="List user by ID.")
    private Long id;

    @Parameter(name=ApiConstants.STATE, type=CommandType.STRING, description="List users by state of the user account.")
    private String state;

    @Parameter(name=ApiConstants.USERNAME, type=CommandType.STRING, description="List user by the username")
    private String username;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////


    public Long getAccountType() {
        return accountType;
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getUsername() {
        return username;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

	@Override
    public String getCommandName() {
        return s_name;
    }
    
    @Override
    public void execute(){
        Pair<List<? extends UserAccount>, Integer> result = _accountService.searchForUsers(this);
        ListResponse<UserResponse> response = new ListResponse<UserResponse>();
        List<UserResponse> userResponses = new ArrayList<UserResponse>();
        for (UserAccount user : result.first()) {
            UserResponse userResponse = _responseGenerator.createUserResponse(user);
            userResponses.add(userResponse);
        }
        response.setResponses(userResponses, result.second());
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
