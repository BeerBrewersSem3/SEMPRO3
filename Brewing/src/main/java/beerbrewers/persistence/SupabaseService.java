package beerbrewers.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupabaseService {
    @Autowired
    private SupabaseConnection supabaseConnection;



}
