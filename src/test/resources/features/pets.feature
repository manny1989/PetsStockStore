Feature: E2E Testing for pets in store

Scenario: Get available pets
 Given Call petstatus api to find "available" pets in store
 When hit petstatus api 
 Then verify the petstatus api response
    
Scenario: Post a new available pet to the store
 Given Call pet api to "add" pets of "987654321" id in store and make it "available"
 When hit pet api to "add" pets 
 Then verify the pet api response and check its availabilty in petStatus api
 
Scenario: Update pet status to sold
 Given Call pet api to "update" pets of "987654321" id in store and make it "sold"
 When hit pet api to "update" pets
 Then verify the pet api response and check its availabilty in petStatus api
 
Scenario: delete pet from store
 Given Call pet api to "delete" pets of "987654321" id in store and make it "delete"
 When hit pet api to "delete" pets
 Then verify the pet api response and check its availabilty in petStatus api