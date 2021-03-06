package com.medicohealthcare.model;

/**
 * Created by Narendra on 20-01-2017.
 */

public class Person implements ServerResponseInterface
{
    public Integer id;
    public String name;
    public Long mobile;
    public String location;
    public Double locationLat;
    public Double locationLong;
    public String email;
    public String address;
    public String allergicTo;
    public String bloodGroup;
    public Long dateOfBirth;
    public Byte gender;
    public String imageUrl;
    public Integer loyaltyPoints;
    public String password;
    public Byte role;
    public Byte status;
    public String speciality;
    public Long validaity;
    public String region;
    public String city;
    public Long registrationDate;
    public Integer addedBy;
    public String country;
    public String isoCountry;
    public Integer loyaltyCatId;
    public Integer referredBy;
    public Byte prime;
    public Integer linkedWith;

    public String practiceName;
    public String registrationNo;

    public Integer errorCode = -1;

    boolean isChanged = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(Double locationLong) {
        this.locationLong = locationLong;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAllergicTo() {
        return allergicTo;
    }

    public void setAllergicTo(String allergicTo) {
        this.allergicTo = allergicTo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getRole() {
        return role;
    }

    public void setRole(Byte role) {
        this.role = role;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Long getValidaity() {
        return validaity;
    }

    public void setValidaity(Long validaity) {
        this.validaity = validaity;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIsoCountry() {
        return isoCountry;
    }

    public void setIsoCountry(String isoCountry) {
        this.isoCountry = isoCountry;
    }

    public Integer getLoyaltyCatId() {
        return loyaltyCatId;
    }

    public void setLoyaltyCatId(Integer loyaltyCatId) {
        this.loyaltyCatId = loyaltyCatId;
    }

    public Integer getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Integer referredBy) {
        this.referredBy = referredBy;
    }

    public Byte getPrime() {
        return prime;
    }

    public void setPrime(Byte prime) {
        this.prime = prime;
    }

    public boolean isChanged()
    {
        return isChanged;
    }
    public void setChanged(boolean changed)
    {
        isChanged = changed;
    }
    public boolean canBeSaved()
    {
        boolean canBeSaved = true;
        if(name == null )
            canBeSaved = false;
        else if(mobile == null || mobile.longValue() > 0 == false)
            canBeSaved = false;
        else if(locationLat == null || locationLat.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(locationLong == null || locationLong.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(email == null )
            canBeSaved = false;
        else if(address == null )
            canBeSaved = false;
        else if(dateOfBirth == null )
            canBeSaved = false;
        else if(gender == null || gender.byteValue() >= 0 == false || gender.byteValue() < 2 == false)
            canBeSaved = false;
        else if(role == null || role.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(status == null || status.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(speciality == null && speciality.trim().length() > 0 == false)
            canBeSaved = false;
        else if(city == null || city.trim().length() > 0 == false)
            canBeSaved = false;
        else if(country == null || country.trim().length() > 0 == false)
            canBeSaved = false;
        else if(prime == null || prime.byteValue() >= 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }
    public boolean canBeSavedForDoctor()
    {
        boolean canBeSaved = true;
        if(name == null )
            canBeSaved = false;
        else if(mobile == null || mobile.longValue() > 0 == false)
            canBeSaved = false;
        else if(locationLat == null || locationLat.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(locationLong == null || locationLong.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(email == null )
            canBeSaved = false;
        else if(address == null )
            canBeSaved = false;
        else if(gender == null || gender.byteValue() >= 0 == false || gender.byteValue() < 2 == false)
            canBeSaved = false;
        else if(role == null || role.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(status == null || status.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(speciality == null && speciality.trim().length() > 0 == false)
            canBeSaved = false;
        else if(city == null || city.trim().length() > 0 == false)
            canBeSaved = false;
        else if(country == null || country.trim().length() > 0 == false)
            canBeSaved = false;
        else if(prime == null || prime.byteValue() >= 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }
    public boolean canBeSavedForUnregisteredPatient()
    {
        boolean canBeSaved = true;
        if(name == null )
            canBeSaved = false;
        else if(mobile == null || mobile.longValue() > 0 == false)
            canBeSaved = false;
        else if(locationLat == null || locationLat.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(locationLong == null || locationLong.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(address == null )
            canBeSaved = false;
        else if(gender == null || gender.byteValue() >= 0 == false || gender.byteValue() < 2 == false)
            canBeSaved = false;
        else if(role == null || role.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(status == null || status.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(speciality == null && speciality.trim().length() > 0 == false)
            canBeSaved = false;
        else if(city == null || city.trim().length() > 0 == false)
            canBeSaved = false;
        else if(country == null || country.trim().length() > 0 == false)
            canBeSaved = false;
        else if(prime == null || prime.byteValue() >= 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }
    public boolean canBeSavedForAssistantRegistration()
    {
        boolean canBeSaved = true;
        if(name == null )
            canBeSaved = false;
        else if(mobile == null || mobile.longValue() > 0 == false)
            canBeSaved = false;
        else if(locationLat == null || locationLat.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(locationLong == null || locationLong.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(email == null )
            canBeSaved = false;
        else if(address == null )
            canBeSaved = false;
        else if(gender == null || gender.byteValue() >= 0 == false || gender.byteValue() < 2 == false)
            canBeSaved = false;
        else if(role == null || role.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(status == null || status.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(speciality == null && speciality.trim().length() > 0 == false)
            canBeSaved = false;
        else if(city == null || city.trim().length() > 0 == false)
            canBeSaved = false;
        else if(country == null || country.trim().length() > 0 == false)
            canBeSaved = false;
        else if(prime == null || prime.byteValue() >= 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }
    public Person()
    {
    }

    public Person(Person person)
    {
        id = person.getId();
        name = person.getName();
        mobile = person.getMobile();
        location = person.getLocation();
        locationLat = person.getLocationLat();
        locationLong = person.getLocationLong();
        email = person.getEmail();
        address = person.getAddress();
        allergicTo = person.getAllergicTo();
        bloodGroup = getBloodGroup();
        dateOfBirth = person.getDateOfBirth();
        gender = person.getGender();
        imageUrl = person.getImageUrl();
        loyaltyPoints = person.getLoyaltyPoints();
        role = person.getRole();
        status = person.getStatus();
        speciality = person.getSpeciality();
        validaity = person.getValidaity();
        region = person.getRegion();
        city = person.getCity();
        registrationDate = person.getRegistrationDate();
        addedBy = person.getAddedBy();
        country = person.getCountry();
        isoCountry = person.getIsoCountry();
        loyaltyCatId = person.getLoyaltyCatId();
        referredBy = person.getReferredBy();
        prime = person.getPrime();
        practiceName = person.practiceName;
        registrationNo = person.registrationNo;
    }

}
