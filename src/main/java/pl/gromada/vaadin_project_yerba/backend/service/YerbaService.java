package pl.gromada.vaadin_project_yerba.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gromada.vaadin_project_yerba.backend.enums.Brand;
import pl.gromada.vaadin_project_yerba.backend.enums.Country;
import pl.gromada.vaadin_project_yerba.backend.exception.YerbaNotFoundException;
import pl.gromada.vaadin_project_yerba.backend.model.Yerba;
import pl.gromada.vaadin_project_yerba.backend.repo.YerbaRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class YerbaService {

    private YerbaRepository yerbaRepository;

    @Autowired
    public YerbaService(YerbaRepository yerbaRepository) {
        this.yerbaRepository = yerbaRepository;
    }

    public List<Yerba> findAllYerba(String nameFilter, String brandFilter, String countryFilter) {
        if ((nameFilter == null || nameFilter.isEmpty()) && (brandFilter == null || brandFilter.equals(""))
                && (countryFilter == null || countryFilter.isEmpty()))
            return yerbaRepository.findAll();
        else
            return yerbaRepository.findAllByFilters(nameFilter, brandFilter, countryFilter);
    }

    public Yerba finYerbaById(long id) {
        return yerbaRepository.findById(id).orElseThrow(() -> new YerbaNotFoundException(id));
    }

    public void saveYerba(Yerba yerba) {
        yerbaRepository.save(yerba);
    }

    public void deleteYerba(Yerba yerba) {
        yerbaRepository.delete(yerba);
    }

    public long countYerba() {
        return yerbaRepository.count();
    }

    public void updateYerba(long id, Yerba yerba) {
        yerba.setIdYerba(id);
        yerbaRepository.save(yerba);
    }

    @PostConstruct
    private void initData() {
        if (yerbaRepository.count() == 0) {
            saveYerba(new Yerba("Compuesta Con Hierbas", Brand.Armada, Country.Argentina,
                    "https://www.poyerbani.pl/pol_pm_Amanda-Compuesta-Con-Hierbas-Ziolowa-0-5kg-891_2.png"));
            saveYerba(new Yerba("Despalada Sin Palo", Brand.Armada, Country.Argentina,
                    "https://www.poyerbani.pl/pol_il_Amanda-Despalada-Sin-Palo-0-5kg-894.png"));
            saveYerba(new Yerba("Katuava + Ginseng", Brand.Campesino, Country.Paraguay,
                    "https://www.poyerbani.pl/pol_il_Campesino-Katuava-Ginseng-0-5kg-986.png"));
            saveYerba(new Yerba("Endulife Con Stevia", Brand.CBSe, Country.Argentina,
                    "https://www.poyerbani.pl/pol_il_CBSe-Endulife-Con-Stevia-0-5kg-1010.png"));
            saveYerba(new Yerba("Energia Guarana ", Brand.CBSe, Country.Argentina,
                    "https://www.poyerbani.pl/pol_il_CBSe-Energia-Guarana-0-5-kg-1016.png"));
            saveYerba(new Yerba("Compuesta Mate Completo", Brand.Colon, Country.Paraguay,
                    "https://www.poyerbani.pl/pol_il_Colon-Compuesta-Mate-Completo-0-5kg-3002.png"));
            saveYerba(new Yerba("Doble Sabor ", Brand.Selecta, Country.Paraguay,
                    "https://www.poyerbani.pl/pol_il_Selecta-Doble-Sabor-0-5kg-1466.png"));
        }
    }
}
