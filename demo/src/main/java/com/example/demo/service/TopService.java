package com.example.demo.service;

import com.example.demo.model.Top;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

@Service
public class TopService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    public List<Top> getAllTops(String sortField, String sortOrder) {
        List<Top> tops = jdbcTemplate.query(
            "SELECT \"Rank\", \"Name\", \"Net Worth\", \"Age\", \"Country\", \"Industry\", \"Score\" FROM public.top",
            (rs, rowNum) -> {
                Top top = new Top();
                top.setRank(rs.getInt("Rank"));
                top.setName(rs.getString("Name"));
                top.setNetWorth(rs.getString("Net Worth"));
                top.setAge(rs.getInt("Age"));
                top.setCountry(rs.getString("Country"));
                top.setIndustry(rs.getString("Industry"));
                top.setScore(rs.getString("Score"));
                return top;
            }
        );
        
        Comparator<Top> comparator = switch (sortField) {
            case "rank" -> Comparator.comparing(Top::getRank);
            case "name" -> Comparator.comparing(Top::getName);
            case "netWorth" -> Comparator.comparing(Top::getNetWorth);
            case "age" -> Comparator.comparing(Top::getAge);
            case "country" -> Comparator.comparing(Top::getCountry);
            case "industry" -> Comparator.comparing(Top::getIndustry);
            case "score" -> Comparator.comparing(Top::getScore);
            default -> Comparator.comparing(Top::getRank);
        };

        if ("desc".equals(sortOrder)) {
            tops.sort(comparator.reversed());
        } else {
            tops.sort(comparator);
        }
        return tops;
    }

    public void addTop(Top top) {
        String sql = "INSERT INTO public.top (\"Rank\", \"Name\", \"Net Worth\", \"Age\", \"Country\", \"Industry\", \"Score\") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
            top.getRank(),
            top.getName(),
            top.getNetWorth(),
            top.getAge(),
            top.getCountry(),
            top.getIndustry(),
            top.getScore()
        );
    }

    public void deleteTop(int rank) {
        String sql = "DELETE FROM public.top WHERE \"Rank\" = ?";
        jdbcTemplate.update(sql, rank);
    }

    public void updateTop(Top top) {
        String sql = "UPDATE public.top SET \"Name\" = ?, \"Net Worth\" = ?, \"Age\" = ?, " +
                    "\"Country\" = ?, \"Industry\" = ?, \"Score\" = ? WHERE \"Rank\" = ?";
        
        jdbcTemplate.update(sql,
            top.getName(),
            top.getNetWorth(),
            top.getAge(),
            top.getCountry(),
            top.getIndustry(),
            top.getScore(),
            top.getRank()
        );
    }

    public String getTask1Result() {
        String sql = "SELECT \"Name\", \"Score\", \"Net Worth\" FROM public.top WHERE \"Country\" = 'United States' AND \"Industry\" = 'Energy' ORDER BY CAST(REPLACE(REPLACE(\"Net Worth\", '$', ''), 'B', '') AS DECIMAL) DESC LIMIT 1";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return String.format("""
                    <div class="result-item">
                        <div class="result-title">Бизнесмен из США в сфере Energy с самым большим капиталом:</div>
                        <div class="result-content">
                            <div class="result-field"><strong>Имя:</strong> %s</div>
                            <div class="result-field"><strong>Компания:</strong> %s</div>
                            <div class="result-field"><strong>Состояние:</strong> %s</div>
                        </div>
                    </div>
                    """, 
                    rs.getString("Name"),
                    rs.getString("Score"),
                    rs.getString("Net Worth")
                );
            }
            return "<div class='result-item'><div class='result-title'>В базе нет совпадений по заданным критериям</div></div>";
        } catch (SQLException e) {
            e.printStackTrace();
            return "<div class='result-item'><div class='result-title'>Ошибка при выполнении запроса</div></div>";
        }
    }

    public String getTask2Result() {
        String sql = "SELECT \"Name\", \"Age\", \"Net Worth\", \"Industry\" FROM public.top WHERE \"Country\" = 'France' AND CAST(REPLACE(REPLACE(\"Net Worth\", '$', ''), 'B', '') AS DECIMAL) > 10 ORDER BY \"Age\" ASC LIMIT 1";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return String.format("""
                    <div class="result-item">
                        <div class="result-title">Самый молодой миллиардер из Франции с капиталом > 10 млрд:</div>
                        <div class="result-content">
                            <div class="result-field"><strong>Имя:</strong> %s</div>
                            <div class="result-field"><strong>Возраст:</strong> %d лет</div>
                            <div class="result-field"><strong>Состояние:</strong> %s</div>
                            <div class="result-field"><strong>Индустрия:</strong> %s</div>
                        </div>
                    </div>
                    """, 
                    rs.getString("Name"),
                    rs.getInt("Age"),
                    rs.getString("Net Worth"),
                    rs.getString("Industry")
                );
            }
            return "<div class='result-item'><div class='result-title'>В базе нет совпадений по заданным критериям</div></div>";
        } catch (SQLException e) {
            e.printStackTrace();
            return "<div class='result-item'><div class='result-title'>Ошибка при выполнении запроса</div></div>";
        }
    }
} 